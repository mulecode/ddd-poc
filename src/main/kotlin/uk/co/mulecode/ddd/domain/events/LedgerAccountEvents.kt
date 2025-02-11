package uk.co.mulecode.ddd.domain.events

import uk.co.mulecode.ddd.domain.model.LedgerAccountModel

class LedgerAccountCreatedEvent(
    val data: LedgerAccountModel
) : DomainEvent()

class LedgerAccountActivatedEvent(
    val data: LedgerAccountModel
) : DomainEvent()
