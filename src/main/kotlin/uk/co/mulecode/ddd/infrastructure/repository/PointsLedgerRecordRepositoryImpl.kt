package uk.co.mulecode.ddd.infrastructure.repository

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import uk.co.mulecode.ddd.domain.model.PointLedgerRecordModel
import uk.co.mulecode.ddd.domain.model.TransactionStatus
import uk.co.mulecode.ddd.domain.model.TransactionType
import uk.co.mulecode.ddd.domain.repository.PointsLedgerRecordRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaPointsLedgerRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.PointsLedgerRecordEntity
import java.util.*

private const val AGGREGATE_ROOT_NAME = "entity"

@Component
class PointsLedgerRecordRepositoryImpl(
    private val jpaPointsLedgerRepository: JpaPointsLedgerRepository
) : PointsLedgerRecordRepository {

    private val log: KLogger = KotlinLogging.logger {}

    @Transactional
    override fun save(pointLedgerRecordModel: PointLedgerRecordModel): PointLedgerRecordModel {
        val entity = toEntity(pointLedgerRecordModel)
        return toModel(jpaPointsLedgerRepository.save(entity))
    }

    @Transactional(readOnly = true)
    override fun getBalance(userId: String): PointLedgerRecordModel {
        log.info { "Getting balance for user $userId" }
        return toModel(
            requireNotNull(
                jpaPointsLedgerRepository.findFirstByUserIdOrderByCreatedAtDesc(userId)
            ) { "No points ledger record found for user $userId" }
        )
    }

    @Transactional(readOnly = true)
    override fun getHistory(userId: String, page: Int, batch: Int): List<PointLedgerRecordModel> {
        val pageable = PageRequest.of(page, batch)
        return jpaPointsLedgerRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable)
            .content
            .map { toModel(it) }
    }

    companion object {
        fun toModel(entity: PointsLedgerRecordEntity, detached: Boolean = true): PointLedgerRecordModel {
            val model = PointLedgerRecordModel(
                id = entity.id,
                userId = entity.userId,
                points = entity.points,
                type = TransactionType.valueOf(entity.transactionType),
                description = entity.description,
                systemDescription = entity.systemDescription,
                status = TransactionStatus.valueOf(entity.transactionStatus),
                balance = entity.balance,
                createdAt = entity.createdAt,
                transactionHash = entity.transactionHash
            )
            if (!detached) {
                model.setInfraContext(AGGREGATE_ROOT_NAME, entity)
            }

            return model
        }

        fun toEntity(model: PointLedgerRecordModel): PointsLedgerRecordEntity {
            return if (model.getInfraContext().containsKey(AGGREGATE_ROOT_NAME)) {
                val entity = model.getInfraContext()[AGGREGATE_ROOT_NAME] as PointsLedgerRecordEntity
                entity.apply {
                    description = model.description
                }
            } else {
                PointsLedgerRecordEntity(
                    id = UUID.randomUUID().toString(),
                    userId = model.userId,
                    points = model.points,
                    transactionType = model.type.name,
                    description = model.description,
                    systemDescription = model.systemDescription,
                    transactionStatus = model.status.name,
                    balance = model.balance,
                    transactionHash = model.transactionHash
                )
            }
        }
    }
}

