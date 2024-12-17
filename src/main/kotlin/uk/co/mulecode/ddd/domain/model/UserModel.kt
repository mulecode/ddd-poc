package uk.co.mulecode.ddd.domain.model

import uk.co.mulecode.ddd.domain.events.DomainEvent
import uk.co.mulecode.ddd.domain.events.UserActivatedEvent
import uk.co.mulecode.ddd.domain.events.UserCreatedEvent

enum class UserStatus {
    ACTIVE,
    INACTIVE
}

class UserModel(
    val id: String,
    val name: String,
    val email: String,
    var status: UserStatus = UserStatus.INACTIVE
) {

    private val domainEvents = mutableListOf<DomainEvent>()

    fun domainEvents(): List<DomainEvent> {
        return domainEvents.toList()
    }

    fun clearDomainEvents() {
        domainEvents.clear()
    }

    fun activateUser() {
        status = UserStatus.ACTIVE
        domainEvents.add(UserActivatedEvent(this))
    }

    companion object {
        @JvmStatic
        fun createUser(name: String, email: String): UserModel {
            val user = UserModel(
                id = java.util.UUID.randomUUID().toString(),
                name = name,
                email = email
            )
            user.domainEvents.add(UserCreatedEvent(user))
            return user
        }
    }
}

