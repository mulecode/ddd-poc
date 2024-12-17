package uk.co.mulecode.ddd.domain.schedulers

import uk.co.mulecode.ddd.domain.events.UserCountScheduledEvent

interface CountUsersScheduler {
    fun countUsers(): UserCountScheduledEvent
}
