package uk.co.mulecode.ddd.infrastructure.repository

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import uk.co.mulecode.ddd.domain.model.LedgerAccountListModel
import uk.co.mulecode.ddd.domain.model.LedgerAccountModel
import uk.co.mulecode.ddd.domain.model.LedgerRecordModel
import uk.co.mulecode.ddd.domain.repository.LedgerAccountRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaLedgerAccountEntity
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaLedgerAccountRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaLedgerRecordEntity
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaLedgerRecordRepository
import java.util.UUID


@Component
class LedgerAccountRepositoryImpl(
    private val eventPublisher: ApplicationEventPublisher,
    private val jpaLedgerAccountRepository: JpaLedgerAccountRepository,
    private val jpaLedgerRecordRepository: JpaLedgerRecordRepository,
) : LedgerAccountRepository {

    private val log = KotlinLogging.logger {}

    @Transactional
    override fun findById(id: UUID, historySize: Int?): LedgerAccountModel {
        log.info { "Repository: Loading ledger account $id" }

        val account = jpaLedgerAccountRepository.findByIdOrNull(id)
            ?: throw IllegalArgumentException("Ledger Account not found for id $id")

        val lastRecord = jpaLedgerRecordRepository.findTopByPayerAccountIdOrderByCreatedDateDesc(account.id)

        val history = historySize
            ?.takeIf { it > 0 }
            ?.let {
                jpaLedgerRecordRepository.findAllByPayerAccountIdOrderByCreatedDateDesc(
                    accountId = account.id, page = Pageable.ofSize(it)
                )
            }

        return LedgerAccountModel(
            data = account,
            lastRecord = LedgerRecordModel(
                data = lastRecord,
                createdAt = lastRecord.createdDate
                    ?: throw IllegalStateException("Ledger Record does not have a creation date")
            ),
            history = history?.content
                ?.sortedBy { it.createdDate }
                ?.zipWithNext { previous, current ->
                    LedgerRecordModel(
                        data = current,
                        previousSignature = previous.verificationSignature,
                        createdAt = current.createdDate
                            ?: throw IllegalStateException("Ledger Record does not have a creation date")
                    )
                }
        )
    }

    @Transactional
    override fun save(model: LedgerAccountModel): LedgerAccountModel {
        log.debug { "Saving LedgerAccountModel: ${model.data.id}" }
        val entity = if (model.data is JpaLedgerAccountRepository) {
            jpaLedgerAccountRepository.save(model.data as JpaLedgerAccountEntity)
        } else {
            jpaLedgerAccountRepository.save(
                JpaLedgerAccountEntity(
                    id = model.data.id,
                    userId = model.data.userId,
                    accountType = model.data.accountType,
                    name = model.data.name,
                    description = model.data.description,
                    status = model.data.status
                )
            )
        }
        log.debug { "LedgerAccountModel saved: ${entity.id}" }
        model.getProspectRecords().forEach {
            log.debug { "Saving LedgerRecord: ${it.id} nonce: ${it.verificationCode} hash: ${it.verificationSignature} raw: ${it.rawSignature()}" }
            jpaLedgerRecordRepository.save(
                JpaLedgerRecordEntity(
                    id = it.id,
                    payerAccountId = it.payerAccountId,
                    payeeAccountId = it.payeeAccountId,
                    amount = it.amount,
                    transactionType = it.transactionType,
                    transactionCategory = it.transactionCategory,
                    referenceId = it.referenceId,
                    balanceSnapshot = it.balanceSnapshot,
                    verificationStatus = it.verificationStatus,
                    verificationCode = it.verificationCode,
                    verificationSignature = it.verificationSignature
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
    override fun findAll(pageable: Pageable): LedgerAccountListModel {
        log.info { "Getting all LedgerAccountModel ${Thread.currentThread().name}" }
        return jpaLedgerAccountRepository.findAll(pageable).let {
            LedgerAccountListModel(
                ledgerAccountList = it.content.map { ledger -> LedgerAccountModel(ledger) },
                page = it.number,
                totalPages = it.totalPages,
                size = it.size,
                totalElements = it.totalElements
            )
        }
    }

}
