package uk.co.mulecode.ddd.domain.model

import uk.co.mulecode.ddd.domain.events.LedgerAccountActivatedEvent
import uk.co.mulecode.ddd.domain.events.LedgerAccountTransactionCreatedEvent
import uk.co.mulecode.ddd.infrastructure.utils.IdentificationGenerator.Companion.sortedUuid
import java.math.BigDecimal
import java.util.UUID

enum class LedgerAccountStatus {
    ACTIVE,
    INACTIVE
}

enum class LedgerAccountType {
    ASSETS,
    LIABILITIES,
    EQUITY,
    REVENUE,
    EXPENSES
}

interface LedgerAccount {
    val id: UUID
    val userId: UUID
    val accountType: LedgerAccountType
    var name: String
    var description: String
    var status: LedgerAccountStatus
}

enum class LedgerState {
    PRISTINE,
    ALTERED
}

class LedgerAccountModel(
    val data: LedgerAccount,
    val lastRecord: LedgerRecordModel? = null,
    val history: List<LedgerRecordModel>? = emptyList()
) : BaseModel() {

    private var state: LedgerState = LedgerState.PRISTINE
    private val prospectRecords = mutableListOf<LedgerRecord>()

    init {
        history?.forEach {
            it.verify()
        }
    }

    fun getProspectRecords(): List<LedgerRecord> {
        return prospectRecords.toList()
    }

    fun activate() {
        data.status = LedgerAccountStatus.ACTIVE
        addEvent(LedgerAccountActivatedEvent(this))
    }

    fun balance(): BigDecimal {
        if (state == LedgerState.PRISTINE) {
            return lastRecord?.data?.balanceSnapshot ?: BigDecimal.ZERO
        }
        return prospectRecords.last().balanceSnapshot
    }

    fun signature(): String {
        if (state == LedgerState.PRISTINE) {
            return lastRecord?.data?.verificationSignature ?: ""
        }
        return prospectRecords.last().verificationSignature
    }

    fun debit(amount: BigDecimal, referenceId: String) {
        val newBalance = balance().plus(amount)
        addTransaction(amount, referenceId, newBalance, TransactionType.DEBIT)
        state = LedgerState.ALTERED
    }

    fun credit(amount: BigDecimal, referenceId: String) {
        val newBalance = balance().minus(amount)
        if (newBalance < BigDecimal.ZERO) {
            throw IllegalStateException("Insufficient funds")
        }
        addTransaction(amount, referenceId, newBalance, TransactionType.CREDIT)
        state = LedgerState.ALTERED
    }

    private fun addTransaction(
        amount: BigDecimal,
        referenceId: String,
        newBalance: BigDecimal,
        transactionType: TransactionType
    ) {

        val verification = VerificationModel(
            previousSignature = signature(),
        )

        val newRecord = LedgerRecordProspect(
            id = sortedUuid(),
            payerAccountId = data.id,
            payeeAccountId = data.id,
            amount = amount,
            referenceId = referenceId,
            transactionType = transactionType,
            transactionCategory = TransactionCategory.STANDARD,
            balanceSnapshot = newBalance,
            verificationSignature = "",
            verificationCode = 0,
            verificationStatus = VerificationStatus.PENDING
        )
        verification.create(data = newRecord.rawSignature())
        newRecord.verificationCode = verification.verificationCode
        newRecord.verificationSignature = verification.verificationSignature
        newRecord.verificationStatus = verification.verificationStatus

        prospectRecords.add(newRecord)
        addEvent(LedgerAccountTransactionCreatedEvent(newRecord))
    }

}

