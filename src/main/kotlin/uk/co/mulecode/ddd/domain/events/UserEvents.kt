package uk.co.mulecode.ddd.domain.events

import uk.co.mulecode.ddd.domain.model.UserModel

class UserCreatedEvent(
    val user: UserModel
) : DomainEvent()

class UserActivatedEvent(
    val user: UserModel
) : DomainEvent()

class UserCountScheduledEvent(
    val total: Int
) : DomainEvent()
