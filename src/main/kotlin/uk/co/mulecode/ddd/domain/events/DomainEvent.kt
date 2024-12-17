package uk.co.mulecode.ddd.domain.events

import java.time.Instant
import java.util.*


abstract class DomainEvent {
    val eventId: UUID = UUID.randomUUID()
    val occurredAt: Instant = Instant.now()
}
