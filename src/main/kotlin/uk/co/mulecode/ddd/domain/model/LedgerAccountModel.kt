package uk.co.mulecode.ddd.domain.model

import uk.co.mulecode.ddd.domain.events.LedgerAccountActivatedEvent
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

class LedgerAccountModel(
    val data: LedgerAccount,
) : BaseModel() {

    fun activate() {
        data.status = LedgerAccountStatus.ACTIVE
        addEvent(LedgerAccountActivatedEvent(this))
    }
}

