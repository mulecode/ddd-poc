package uk.co.mulecode.ddd.domain.events

import uk.co.mulecode.ddd.domain.model.LedgerAccountModel
import uk.co.mulecode.ddd.domain.model.LedgerCoreRecord

class LedgerAccountCreatedEvent(
    val data: LedgerAccountModel
) : DomainEvent()

class LedgerAccountActivatedEvent(
    val data: LedgerAccountModel
) : DomainEvent()

class LedgerAccountTransactionCreatedEvent(
    val data: LedgerCoreRecord
) : DomainEvent()
