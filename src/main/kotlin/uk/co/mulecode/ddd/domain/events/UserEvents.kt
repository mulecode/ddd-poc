package uk.co.mulecode.ddd.domain.events

import uk.co.mulecode.ddd.domain.model.UserBaseModel

class UserCreatedEvent(
    val user: UserBaseModel
) : DomainEvent()

class UserActivatedEvent(
    val user: UserBaseModel
) : DomainEvent()
