package uk.co.mulecode.ddd.domain.model

import uk.co.mulecode.ddd.domain.events.DomainEvent

abstract class BaseModel {

    private val domainEvents = mutableListOf<DomainEvent>()

    fun domainEvents(): List<DomainEvent> {
        return domainEvents.toList()
    }

    fun clearDomainEvents() {
        domainEvents.clear()
    }

    protected fun addEvent(event: DomainEvent) {
        domainEvents.add(event)
    }
}

interface ModelListPageDetails {
    val page: Int
    val totalPages: Int
    val size: Int
    val totalElements: Long
}
