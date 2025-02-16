package uk.co.mulecode.ddd.infrastructure.repository

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import uk.co.mulecode.ddd.domain.model.LedgerAccountModel
import uk.co.mulecode.ddd.domain.model.LedgerAccountStatus
import uk.co.mulecode.ddd.domain.model.LedgerAccountType
import uk.co.mulecode.ddd.domain.model.LedgerRecordModel
import uk.co.mulecode.ddd.domain.model.VerificationModel
import uk.co.mulecode.ddd.domain.model.VerificationStatus
import uk.co.mulecode.ddd.domain.repository.LedgerAccountRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaLedgerAccountEntity
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaLedgerAccountRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaLedgerRecordEntity
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaLedgerRecordRepository
import uk.co.mulecode.ddd.infrastructure.utils.IdentificationGenerator.Companion.sortedUuid
import java.util.UUID


@Component
class LedgerAccountRepositoryImpl(
    private val eventPublisher: ApplicationEventPublisher,
    private val jpaLedgerAccountRepository: JpaLedgerAccountRepository,
    private val jpaLedgerRecordRepository: JpaLedgerRecordRepository,
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
                id = sortedUuid(),
                userId = userId,
                accountType = type,
                name = name,
                description = description,
                status = LedgerAccountStatus.INACTIVE
            )
        )
        return LedgerAccountModel(
            data = account,
        )
    }

    @Transactional
    override fun findById(id: UUID, historySize: Int?): LedgerAccountModel {
        log.info { "Repository: Loading ledger account $id" }

        val account = jpaLedgerAccountRepository.findByIdOrNull(id)
            ?: throw IllegalArgumentException("Ledger Account not found for id $id")

        val lastRecord = jpaLedgerRecordRepository.findTopByPayerAccountIdOrderByIdDesc(account.id)

        val history = historySize
            ?.takeIf { it > 0 }
            ?.let {
                jpaLedgerRecordRepository.findAllByPayerAccountIdOrderByIdDesc(
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
                ?.sortedByDescending { it.createdDate }
                ?.zipWithNext { current, previous ->
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
        val entity = jpaLedgerAccountRepository.save(model.data as JpaLedgerAccountEntity)
        log.debug { "LedgerAccountModel saved: ${entity.id}" }

        var lastVerificationSignature = model.lastRecord?.data?.verificationSignature ?: ""

        model.getProspectRecords().forEach {
            log.debug { "Saving LedgerRecord: ${it.id}" }

            val verification = VerificationModel(previousSignature = lastVerificationSignature)
            verification.create(data = it.rawSignature())

            jpaLedgerRecordRepository.save(
                JpaLedgerRecordEntity(
                    id = sortedUuid(),
                    payerAccountId = it.payerAccountId,
                    payeeAccountId = it.payeeAccountId,
                    amount = it.amount,
                    transactionType = it.transactionType,
                    transactionCategory = it.transactionCategory,
                    referenceId = it.referenceId,
                    balanceSnapshot = it.balanceSnapshot,
                    verificationStatus = VerificationStatus.VERIFIED,
                    verificationCode = verification.verificationCode,
                    verificationSignature = verification.verificationSignature
                )
            ).let { savedRecord ->
                lastVerificationSignature = savedRecord.verificationSignature
            }
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
        return jpaLedgerAccountRepository.findAll().map { LedgerAccountModel(it) }
    }

}
