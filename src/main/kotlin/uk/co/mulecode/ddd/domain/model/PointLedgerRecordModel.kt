package uk.co.mulecode.ddd.domain.model

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import uk.co.mulecode.ddd.domain.model.TransactionCategory.STANDARD
import uk.co.mulecode.ddd.domain.model.TransactionStatus.PROCESSED
import uk.co.mulecode.ddd.domain.model.TransactionType.CREDIT
import uk.co.mulecode.ddd.domain.model.TransactionType.DEBIT
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.util.*
import java.util.UUID.randomUUID

enum class TransactionType {
    DEBIT, // Amount being added to the account
    CREDIT // Amount being deducted from the account
}

enum class TransactionStatus {
    PROCESSED,
    PENDING,
    FAILED
}

enum class TransactionCategory {
    STANDARD,   // – A regular credit or debit transaction.
    REVERSAL,   // – A transaction that negates a previous one.
    REFUND,     // – A customer-initiated refund.
    CHARGEBACK, // – A dispute-initiated reversal.
    FEE,        // – A system-imposed fee (e.g., processing fee).
    ADJUSTMENT, // – A manual correction (if applicable).
}


class LedgerRecordModelBuilder(
    // Identity fields
    val userId: UUID,
    val payerAccountId: String?,
    val payeeAccountId: String?,
    val linkedTransactionId: UUID?,
    val referenceId: String,
    // Transaction fields
    var amount: BigDecimal,
    val transactionType: TransactionType,
    val transactionCategory: TransactionCategory,
    val balanceSnapshot: BigDecimal,
    val transactionStatus: TransactionStatus,
    // Metadata fields generic values
    var metadata: Map<String, String>,
    // previous transaction
    val previousId: UUID?,
    val previousTransactionHash: String? = "",
) {

    companion object {
        const val BLOCKCHAIN_DIFFICULTY = 4
    }

    fun create(): PointLedgerRecordModel {
        val id = randomUUID()
        val createdAt = now()

        val blockChain = runBlocking {
            mineNonce(
                data = blockChainData(
                    id = id,
                    createdAt = createdAt,
                ),
                difficulty = BLOCKCHAIN_DIFFICULTY
            )
        }

        return PointLedgerRecordModel(
            id = id,
            userId = userId,
            payerAccountId = payerAccountId,
            payeeAccountId = payeeAccountId,
            linkedTransactionId = linkedTransactionId,
            referenceId = referenceId,
            amount = amount,
            transactionType = transactionType,
            transactionCategory = transactionCategory,
            balanceSnapshot = balanceSnapshot,
            transactionStatus = transactionStatus,
            metadata = metadata,
            transactionNonce = blockChain.first,
            transactionHash = blockChain.second,
            createdAt = createdAt,
            previousId = previousId,
        )
    }

    private fun blockChainData(
        id: UUID,
        createdAt: LocalDateTime
    ): String {
        return """
            |$id
            |$userId
            |$payerAccountId
            |$payeeAccountId
            |$linkedTransactionId
            |$referenceId
            |$amount
            |$transactionType
            |$transactionCategory
            |$balanceSnapshot
            |$transactionStatus
            |$createdAt
            |$previousId
            |$previousTransactionHash"
        """.trimIndent()
    }

    private suspend fun mineNonce(
        data: String,
        difficulty: Int,
        numWorkers: Int = Runtime.getRuntime().availableProcessors()
    ): Pair<Int, String> {
        val targetPrefix = "0".repeat(difficulty)
        val channel = Channel<Pair<Int, String>>()
        val parentJob = SupervisorJob()
        val scope = CoroutineScope(Dispatchers.Default + parentJob)

        repeat(numWorkers) { workerId ->
            scope.launch {
                var nonce = workerId
                while (true) {
                    val combined = "$data$nonce"
                    val hash = hashString(combined)
                    if (hash.startsWith(targetPrefix)) {
                        channel.send(Pair(nonce, hash))
                        return@launch
                    }
                    nonce += numWorkers
                }
            }
        }

        val result = channel.receive()
        parentJob.cancel()
        return result
    }

    private fun hashString(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}


class PointLedgerRecordModel(
    // Identity fields
    val id: UUID,
    val previousId: UUID?,
    val userId: UUID,
    val payerAccountId: String?,
    val payeeAccountId: String?,
    val linkedTransactionId: UUID?,
    val referenceId: String,
    // Transaction fields
    var amount: BigDecimal,
    val transactionType: TransactionType,
    val transactionCategory: TransactionCategory,
    val balanceSnapshot: BigDecimal,
    val transactionStatus: TransactionStatus,
    // Metadata fields generic values
    var metadata: Map<String, String>,
    // Blockchain fields
    val transactionNonce: Int,
    val transactionHash: String,
    // Audit fields
    val createdAt: LocalDateTime,
    // Optimistic locking
    val version: Int = 0,
) {

    private val infraContext = mutableMapOf<String, Any>()

    fun debit(amount: BigDecimal, referenceId: String): PointLedgerRecordModel {
        val newBalance = this.balanceSnapshot.plus(amount)
        return LedgerRecordModelBuilder(
            userId = userId,
            payerAccountId = null,
            payeeAccountId = null,
            linkedTransactionId = null,
            referenceId = referenceId,
            amount = amount,
            transactionType = DEBIT,
            transactionCategory = STANDARD,
            balanceSnapshot = newBalance,
            transactionStatus = PROCESSED,
            metadata = mapOf(),
            previousId = id,
            previousTransactionHash = transactionHash
        ).create()
    }

    fun credit(amount: BigDecimal, referenceId: String): PointLedgerRecordModel {
        val newBalance = this.balanceSnapshot.minus(amount)
        return LedgerRecordModelBuilder(
            userId = userId,
            payerAccountId = null,
            payeeAccountId = null,
            linkedTransactionId = null,
            referenceId = referenceId,
            amount = amount,
            transactionType = CREDIT,
            transactionCategory = STANDARD,
            balanceSnapshot = newBalance,
            transactionStatus = PROCESSED,
            metadata = mapOf(),
            previousId = id,
            previousTransactionHash = transactionHash
        ).create()
    }

    fun isBlockValid(): Boolean {
        val targetPrefix = "0".repeat(LedgerRecordModelBuilder.BLOCKCHAIN_DIFFICULTY)
        return transactionHash.startsWith(targetPrefix)
    }

    fun getInfraContext(): MutableMap<String, Any> {
        return infraContext
    }

    fun setInfraContext(key: String, value: Any) {
        infraContext[key] = value
    }

    companion object {

        private val log = KotlinLogging.logger { }

        @JvmStatic
        fun initiateLedger(userId: UUID): PointLedgerRecordModel {
            log.debug { "Initiating ledger for user $userId" }
            return LedgerRecordModelBuilder(
                userId = userId,
                payerAccountId = null,
                payeeAccountId = null,
                linkedTransactionId = null,
                referenceId = "Account initiated",
                amount = ZERO,
                transactionType = DEBIT,
                transactionCategory = STANDARD,
                balanceSnapshot = ZERO,
                transactionStatus = PROCESSED,
                metadata = mapOf(),
                previousId = null,
                previousTransactionHash = ""
            ).create()
        }
    }

}
