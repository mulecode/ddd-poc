package uk.co.mulecode.ddd.domain.model

import uk.co.mulecode.ddd.domain.events.DomainEvent
import uk.co.mulecode.ddd.domain.events.UserActivatedEvent
import java.util.UUID

enum class UserStatus {
    ACTIVE,
    INACTIVE
}

interface User {
    val id: UUID
    var name: String
    var email: String
    var status: UserStatus
}

class UserModel(
    val data: User,
) {

    private val domainEvents = mutableListOf<DomainEvent>()

    fun domainEvents(): List<DomainEvent> {
        return domainEvents.toList()
    }

    fun clearDomainEvents() {
        domainEvents.clear()
    }

    fun activateUser() {
        data.status = UserStatus.ACTIVE
        domainEvents.add(UserActivatedEvent(this))
    }

}

