package uk.co.mulecode.ddd.domain.model

import java.math.BigDecimal
import java.util.UUID


interface LedgerRecord : VerificationVo {
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

class LedgerRecordModel(
    val data: LedgerRecord,
) : BaseModel() {

}

data class LedgerProspectRecord(
    override val id: UUID,
    override val payerAccountId: UUID,
    override val payeeAccountId: UUID,
    override val referenceId: String,
    override val amount: BigDecimal,
    override val transactionType: TransactionType,
    override val transactionCategory: TransactionCategory,
    override val balanceSnapshot: BigDecimal,
    override val verificationSignature: String,
    override val verificationCode: Int,
    override val verificationStatus: VerificationStatus,
) : LedgerRecord
