package uk.co.mulecode.ddd.infrastructure.repository

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import uk.co.mulecode.ddd.application.utils.JsonUtils
import uk.co.mulecode.ddd.domain.model.PointLedgerRecordModel
import uk.co.mulecode.ddd.domain.repository.LedgerRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaLedgerRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.LedgerRecordEntity
import java.time.LocalDateTime
import java.util.UUID

private const val AGGREGATE_ROOT_NAME = "entity"

@Component
class LedgerRepositoryImpl(
    private val jpaLedgerRepository: JpaLedgerRepository
) : LedgerRepository {

    private val log: KLogger = KotlinLogging.logger {}

    @Transactional
    override fun save(pointLedgerRecordModel: PointLedgerRecordModel): PointLedgerRecordModel {
        val entity = toEntity(pointLedgerRecordModel)
        return toModel(jpaLedgerRepository.save(entity))
    }

    @Transactional(readOnly = true)
    override fun getBalance(userId: UUID): PointLedgerRecordModel {
        log.info { "Getting balance for user $userId" }
        return toModel(
            requireNotNull(
                jpaLedgerRepository.findFirstByUserIdOrderByCreatedAtDesc(userId)
            ) { "No points ledger record found for user: $userId" }
        )
    }

    @Transactional(readOnly = true)
    override fun getHistory(userId: UUID, page: Int, batch: Int): List<PointLedgerRecordModel> {
        val pageable = PageRequest.of(page, batch)
        return jpaLedgerRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable)
            .content.map { toModel(it) }
    }

    companion object {
        fun toModel(entity: LedgerRecordEntity, detached: Boolean = true): PointLedgerRecordModel {
            val model = PointLedgerRecordModel(
                id = UUID.fromString(entity.id),
                previousId = entity.previousId?.let { UUID.fromString(it) },
                userId = entity.userId,
                payerAccountId = entity.payerAccountId,
                payeeAccountId = entity.payeeAccountId,
                linkedTransactionId = entity.linkedTransactionId?.let { UUID.fromString(it) },
                referenceId = entity.referenceId,
                amount = entity.amount,
                transactionType = entity.transactionType,
                transactionCategory = entity.transactionCategory,
                balanceSnapshot = entity.balanceSnapshot,
                transactionStatus = entity.transactionStatus,
                metadata = JsonUtils.fromJsonToMap(entity.metadata),
                transactionNonce = entity.transactionNonce,
                transactionHash = entity.transactionHash,
                createdAt = entity.createdAt,
                version = entity.version
            )
            if (!detached) {
                model.setInfraContext(AGGREGATE_ROOT_NAME, entity)
            }
            return model
        }

        fun toEntity(model: PointLedgerRecordModel): LedgerRecordEntity {
            if (model.getInfraContext().containsKey(AGGREGATE_ROOT_NAME)) {
                throw IllegalStateException("Entity is immutable and cannot be modified")
            } else {
                return LedgerRecordEntity(
                    id = model.id.toString(),
                    previousId = model.previousId?.toString(),
                    userId = model.userId,
                    payerAccountId = model.payerAccountId,
                    payeeAccountId = model.payeeAccountId,
                    linkedTransactionId = model.linkedTransactionId?.toString(),
                    referenceId = model.referenceId,
                    amount = model.amount,
                    transactionType = model.transactionType,
                    transactionCategory = model.transactionCategory,
                    balanceSnapshot = model.balanceSnapshot,
                    transactionStatus = model.transactionStatus,
                    metadata = JsonUtils.toJson(model.metadata),
                    transactionNonce = model.transactionNonce,
                    transactionHash = model.transactionHash,
                    createdAt = LocalDateTime.now(),
                )
            }
        }
    }
}

