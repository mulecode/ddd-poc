package uk.co.mulecode.ddd.infrastructure.repository

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import uk.co.mulecode.ddd.domain.model.LedgerAccountModel
import uk.co.mulecode.ddd.domain.model.LedgerAccountStatus
import uk.co.mulecode.ddd.domain.model.LedgerAccountType
import uk.co.mulecode.ddd.domain.repository.LedgerAccountRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaLedgerAccountRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.LedgerAccountEntity
import java.util.UUID
import java.util.UUID.randomUUID


@Component
class LedgerAccountRepositoryImpl(
    private val eventPublisher: ApplicationEventPublisher,
    private val jpaLedgerAccountRepository: JpaLedgerAccountRepository
) : LedgerAccountRepository {

    private val log = KotlinLogging.logger {}

    @Transactional
    override fun create(
        userId: UUID,
        type: LedgerAccountType,
        name: String,
        description: String
    ): LedgerAccountModel {
        log.info { "Creating new ledger account" }
        return LedgerAccountModel(
            jpaLedgerAccountRepository.save(
                LedgerAccountEntity(
                    id = randomUUID(),
                    userId = userId,
                    accountType = type,
                    name = name,
                    description = description,
                    status = LedgerAccountStatus.INACTIVE
                )
            )
        )
    }

    @Transactional(readOnly = true)
    override fun findById(id: UUID): LedgerAccountModel {
        log.info { "Repository: Loading ledger account $id" }
        return jpaLedgerAccountRepository.findByIdOrNull(id)
            ?.let { LedgerAccountModel(it) }
            ?: throw IllegalArgumentException("Ledger Account not found for id $id")
    }

    @Transactional
    override fun save(model: LedgerAccountModel): LedgerAccountModel {
        log.info { "Saving LedgerAccountModel: ${model.data.id}" }
        val entity = jpaLedgerAccountRepository.save(model.data as LedgerAccountEntity)
        log.info { "User saved: ${entity.id}" }
        model.domainEvents().forEach {
            eventPublisher.publishEvent(it)
        }
        model.clearDomainEvents()
        return model
    }

    @Transactional(readOnly = true)
    override fun findAll(): List<LedgerAccountModel> {
        log.info { "Getting all LedgerAccountModel ${Thread.currentThread().name}" }
        return jpaLedgerAccountRepository.findAll()
            .map { LedgerAccountModel(it) }
    }

}
