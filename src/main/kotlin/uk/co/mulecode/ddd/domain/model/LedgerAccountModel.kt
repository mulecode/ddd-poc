package uk.co.mulecode.ddd.domain.model

import uk.co.mulecode.ddd.domain.events.LedgerAccountActivatedEvent
import uk.co.mulecode.ddd.domain.events.LedgerAccountTransactionCreatedEvent
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
    val lastRecord: LedgerRecord? = null
) : BaseModel() {

    private var state: LedgerState = LedgerState.PRISTINE
    private val prospectRecords = mutableListOf<LedgerProspectRecord>()

    fun getProspectRecords(): List<LedgerProspectRecord> {
        return prospectRecords.toList()
    }

    // The Current balance is the balance of the last record or 0 if there is no record
    private var currentBalance = lastRecord?.balanceSnapshot ?: BigDecimal.ZERO

    fun activate() {
        data.status = LedgerAccountStatus.ACTIVE
        addEvent(LedgerAccountActivatedEvent(this))
    }

    fun balance(): BigDecimal {
        if (state == LedgerState.PRISTINE) {
            return lastRecord?.balanceSnapshot
                ?: throw IllegalStateException("Invalid balance state")
        }
        return currentBalance!!
    }

    fun debit(amount: BigDecimal, referenceId: String) {
        currentBalance = currentBalance?.plus(amount)
        addTransaction(amount, referenceId, TransactionType.DEBIT)
        state = LedgerState.ALTERED
    }

    fun credit(amount: BigDecimal, referenceId: String) {
        val balance = currentBalance?.minus(amount)
        if (balance!! < BigDecimal.ZERO) {
            throw IllegalStateException("Insufficient funds")
        }
        currentBalance = balance
        addTransaction(amount, referenceId, TransactionType.CREDIT)
        state = LedgerState.ALTERED
    }

    private fun addTransaction(amount: BigDecimal, referenceId: String, transactionType: TransactionType) {
        val newRecord = LedgerProspectRecord(
            id = UUID.randomUUID(),
            payerAccountId = data.id,
            payeeAccountId = data.id,
            amount = amount,
            referenceId = referenceId,
            transactionType = transactionType,
            transactionCategory = TransactionCategory.STANDARD,
            balanceSnapshot = currentBalance!!,
            verificationSignature = "",
            verificationCode = 0,
            verificationStatus = VerificationStatus.PENDING
        )
        prospectRecords.add(newRecord)
        addEvent(LedgerAccountTransactionCreatedEvent(newRecord))
    }

}

