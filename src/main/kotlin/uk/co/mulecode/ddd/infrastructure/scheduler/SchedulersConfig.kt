package uk.co.mulecode.ddd.infrastructure.scheduler

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class SchedulersConfig(
    private val countUsersSchedulerImpl: CountUsersSchedulerImpl
) {

    @Scheduled(fixedRate = 60000) // Runs every 60 seconds
    fun scheduleCountUsers() {
        countUsersSchedulerImpl.generateReport()
    }
}
