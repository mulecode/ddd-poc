package uk.co.mulecode.ddd.domain.model

import uk.co.mulecode.ddd.domain.events.DomainEvent
import uk.co.mulecode.ddd.domain.events.UserActivatedEvent
import uk.co.mulecode.ddd.domain.events.UserCreatedEvent
import java.util.*

enum class UserStatus {
    ACTIVE,
    INACTIVE
}

class UserModel(
    val id: UUID,
    val name: String,
    val email: String,
    var status: UserStatus = UserStatus.INACTIVE
) {

    private val infraContext = mutableMapOf<String, Any>()
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

    fun getInfraContext(): MutableMap<String, Any> {
        return infraContext
    }

    fun setInfraContext(key: String, value: Any) {
        infraContext[key] = value
    }

    companion object {
        @JvmStatic
        fun createUser(name: String, email: String): UserModel {
            val user = UserModel(
                id = UUID.randomUUID(),
                name = name,
                email = email
            )
            user.domainEvents.add(UserCreatedEvent(user))
            return user
        }
    }
}

