package uk.co.mulecode.ddd.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

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

class PointLedgerRecordModel(
    // Identity fields
    val id: UUID,
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
        return PointLedgerRecordModel(
            id = UUID.randomUUID(),
            userId = userId,
            payerAccountId = null,
            payeeAccountId = null,
            linkedTransactionId = null,
            referenceId = referenceId,
            amount = amount,
            transactionType = TransactionType.DEBIT,
            transactionCategory = TransactionCategory.STANDARD,
            balanceSnapshot = newBalance,
            transactionStatus = TransactionStatus.PROCESSED,
            metadata = mapOf(),
            transactionNonce = 0,
            transactionHash = "",
            createdAt = LocalDateTime.now(),
        )
    }

    fun credit(amount: BigDecimal, referenceId: String): PointLedgerRecordModel {
        val newBalance = this.balanceSnapshot.minus(amount)
        return PointLedgerRecordModel(
            id = UUID.randomUUID(),
            userId = userId,
            payerAccountId = null,
            payeeAccountId = null,
            linkedTransactionId = null,
            referenceId = referenceId,
            amount = amount,
            transactionType = TransactionType.CREDIT,
            transactionCategory = TransactionCategory.STANDARD,
            balanceSnapshot = newBalance,
            transactionStatus = TransactionStatus.PROCESSED,
            metadata = mapOf(),
            transactionNonce = 0,
            transactionHash = "",
            createdAt = LocalDateTime.now(),
        )
    }

    fun getInfraContext(): MutableMap<String, Any> {
        return infraContext
    }

    fun setInfraContext(key: String, value: Any) {
        infraContext[key] = value
    }

    companion object {
        @JvmStatic
        fun initiateLedger(userId: UUID): PointLedgerRecordModel {
            return PointLedgerRecordModel(
                id = UUID.randomUUID(),
                userId = userId,
                payerAccountId = null,
                payeeAccountId = null,
                linkedTransactionId = null,
                referenceId = "Account initiated",
                amount = BigDecimal.ZERO,
                transactionType = TransactionType.CREDIT,
                transactionCategory = TransactionCategory.STANDARD,
                balanceSnapshot = BigDecimal.ZERO,
                transactionStatus = TransactionStatus.PROCESSED,
                metadata = mapOf(),
                transactionNonce = 0,
                transactionHash = "",
                createdAt = LocalDateTime.now(),
            )
        }

    }
}
