package uk.co.mulecode.ddd.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

enum class TransactionType {
    DEBIT, // Amount being added to the account
    CREDIT // Amount being deducted from the account
}

enum class TransactionCategory {
    STANDARD,   // – A regular credit or debit transaction.
    REVERSAL,   // – A transaction that negates a previous one.
    REFUND,     // – A customer-initiated refund.
    CHARGEBACK, // – A dispute-initiated reversal.
    FEE,        // – A system-imposed fee (e.g., processing fee).
    ADJUSTMENT, // – A manual correction (if applicable).
}

interface LedgerCoreRecord {
    val id: UUID
    val payerAccountId: UUID
    val payeeAccountId: UUID
    val referenceId: String
    val amount: BigDecimal
    val transactionType: TransactionType
    val transactionCategory: TransactionCategory
    val balanceSnapshot: BigDecimal

    fun rawSignature(): String {
        return """
            |$id|
            |$payerAccountId|
            |$payeeAccountId|
            |$referenceId|
            |$amount|
            |$transactionType|
            |$transactionCategory|
            |$balanceSnapshot|
        """.trimIndent()
    }
}

interface LedgerRecord : LedgerCoreRecord, VerificationDetails

data class LedgerRecordProspect(
    override val id: UUID,
    override val payerAccountId: UUID,
    override val payeeAccountId: UUID,
    override val referenceId: String,
    override val amount: BigDecimal,
    override val transactionType: TransactionType,
    override val transactionCategory: TransactionCategory,
    override val balanceSnapshot: BigDecimal,
    override var verificationSignature: String,
    override var verificationCode: Int,
    override var verificationStatus: VerificationStatus,
) : LedgerRecord


class LedgerRecordModel(
    val data: LedgerRecord,
    val previousSignature: String? = null,
    val createdAt: LocalDateTime
) : BaseModel() {

    var status: VerificationStatus? = null

    fun verify() {
        val raw = data.rawSignature()
        val verification = VerificationModel(previousSignature = previousSignature ?: "")
        verification.create(raw)
        status = if (verification.verificationSignature == data.verificationSignature) {
            VerificationStatus.VERIFIED
        } else {
            VerificationStatus.FAILED
        }
    }
}

