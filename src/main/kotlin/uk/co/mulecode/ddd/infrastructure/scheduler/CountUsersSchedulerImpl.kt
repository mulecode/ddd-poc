package uk.co.mulecode.ddd.infrastructure.scheduler

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import uk.co.mulecode.ddd.domain.events.UserCountScheduledEvent
import uk.co.mulecode.ddd.domain.schedulers.CountUsersScheduler
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaUserRepository

@Component
class CountUsersSchedulerImpl(
    private val eventPublisher: ApplicationEventPublisher,
    private val jpaUserRepository: JpaUserRepository
) : CountUsersScheduler {

    private val log = KotlinLogging.logger { }

    @Scheduled(fixedRate = 100000) // 100 seconds
    override fun countUsers(): UserCountScheduledEvent {
        log.info { "#Scheduler#CountUsers" }
        val total = jpaUserRepository.count().toInt()
        val event = UserCountScheduledEvent(total = total)
        eventPublisher.publishEvent(event)
        return event
    }

}
