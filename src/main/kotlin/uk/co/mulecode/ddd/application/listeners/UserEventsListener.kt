package uk.co.mulecode.ddd.application.listeners

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener
import uk.co.mulecode.ddd.domain.events.UserActivatedEvent
import uk.co.mulecode.ddd.domain.events.UserCreatedEvent

@Component
class UserEventsListener {

    private val log = KotlinLogging.logger { }

    @TransactionalEventListener
    fun onUserCreated(userCreated: UserCreatedEvent) {
        log.info { "User created: ${userCreated.user}" }
    }

    @TransactionalEventListener
    fun onUserActivated(userActivated: UserActivatedEvent) {
        log.info { "User activated: ${userActivated.user}" }
    }

}
