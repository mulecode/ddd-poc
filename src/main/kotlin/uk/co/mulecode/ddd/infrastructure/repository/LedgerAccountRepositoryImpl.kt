package uk.co.mulecode.ddd.infrastructure.repository

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import uk.co.mulecode.ddd.domain.model.LedgerAccountModel
import uk.co.mulecode.ddd.domain.model.LedgerAccountStatus
import uk.co.mulecode.ddd.domain.model.LedgerAccountType
import uk.co.mulecode.ddd.domain.model.TransactionCategory
import uk.co.mulecode.ddd.domain.model.TransactionType
import uk.co.mulecode.ddd.domain.repository.LedgerAccountRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaLedgerAccountEntity
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaLedgerAccountRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaLedgerRecordEntity
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaLedgerRecordRepository
import java.math.BigDecimal
import java.util.UUID
import java.util.UUID.randomUUID


@Component
class LedgerAccountRepositoryImpl(
    private val eventPublisher: ApplicationEventPublisher,
    private val jpaLedgerAccountRepository: JpaLedgerAccountRepository,
    private val jpaLedgerRecordRepository: JpaLedgerRecordRepository
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
        val account = jpaLedgerAccountRepository.save(
            JpaLedgerAccountEntity(
                id = randomUUID(),
                userId = userId,
                accountType = type,
                name = name,
                description = description,
                status = LedgerAccountStatus.INACTIVE
            )
        )
        val initialRecord = jpaLedgerRecordRepository.save(
            JpaLedgerRecordEntity(
                id = randomUUID(),
                payerAccountId = account.id,
                payeeAccountId = account.id,
                amount = BigDecimal.ZERO,
                transactionType = TransactionType.DEBIT,
                transactionCategory = TransactionCategory.STANDARD,
                referenceId = "Account opening",
                balanceSnapshot = BigDecimal.ZERO,
            )
        )
        return LedgerAccountModel(
            data = account,
            lastRecord = initialRecord
        )
    }

    @Transactional
    override fun findById(id: UUID): LedgerAccountModel {
        log.info { "Repository: Loading ledger account $id" }

        val account = jpaLedgerAccountRepository.findByIdOrNull(id)
            ?: throw IllegalArgumentException("Ledger Account not found for id $id")

        val lastRecord = jpaLedgerRecordRepository.findFirstByPayerAccountId(account.id)

        return LedgerAccountModel(
            data = account,
            lastRecord = lastRecord
        )
    }

    @Transactional
    override fun save(model: LedgerAccountModel): LedgerAccountModel {
        log.debug { "Saving LedgerAccountModel: ${model.data.id}" }
        val entity = jpaLedgerAccountRepository.save(model.data as JpaLedgerAccountEntity)
        log.debug { "LedgerAccountModel saved: ${entity.id}" }
        model.getProspectRecords().forEach {
            log.debug { "Saving LedgerRecord: ${it.id}" }
            jpaLedgerRecordRepository.save(
                JpaLedgerRecordEntity(
                    id = it.id,
                    payerAccountId = it.payerAccountId,
                    payeeAccountId = it.payeeAccountId,
                    amount = it.amount,
                    transactionType = it.transactionType,
                    transactionCategory = it.transactionCategory,
                    referenceId = it.referenceId,
                    balanceSnapshot = it.balanceSnapshot
                )
            )
        }
        model.domainEvents().forEach {
            eventPublisher.publishEvent(it)
        }
        model.clearDomainEvents()
        return findById(entity.id)
    }

    @Transactional(readOnly = true)
    override fun findAll(): List<LedgerAccountModel> {
        log.info { "Getting all LedgerAccountModel ${Thread.currentThread().name}" }
        return jpaLedgerAccountRepository.findAll()
            .map { LedgerAccountModel(it) }
    }

}
