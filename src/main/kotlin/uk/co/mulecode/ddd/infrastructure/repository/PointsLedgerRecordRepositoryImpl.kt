package uk.co.mulecode.ddd.infrastructure.repository

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import uk.co.mulecode.ddd.application.utils.JsonUtils
import uk.co.mulecode.ddd.domain.model.PointLedgerRecordModel
import uk.co.mulecode.ddd.domain.repository.PointsLedgerRecordRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaLedgerRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.LedgerRecordEntity
import java.security.MessageDigest
import java.time.LocalDateTime
import java.util.UUID

private const val AGGREGATE_ROOT_NAME = "entity"

@Component
class PointsLedgerRecordRepositoryImpl(
    private val jpaLedgerRepository: JpaLedgerRepository
) : PointsLedgerRecordRepository {

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
                jpaLedgerRepository.findFirstByUserIdOrderByCreatedAtDesc(userId.toString())
            ) { "No points ledger record found for user: $userId" }
        )
    }

    @Transactional(readOnly = true)
    override fun getHistory(userId: UUID, page: Int, batch: Int): List<PointLedgerRecordModel> {
        val pageable = PageRequest.of(page, batch)
        return jpaLedgerRepository.findAllByUserIdOrderByCreatedAtDesc(userId.toString(), pageable)
            .content.map { toModel(it) }
    }

    /**
     * Mines a nonce for a given data string that meets the specified difficulty.
     *
     * This function uses multiple coroutines to find a nonce that, when combined with the input data,
     * produces a SHA-256 hash with a prefix of a specified number of zeroes (difficulty).
     *
     * @param data The input data string to be hashed with the nonce.
     * @param difficulty The number of leading zeroes required in the hash.
     * @param numWorkers The number of worker coroutines to use for mining. Defaults to the number of available processors.
     * @return A pair containing the nonce and the resulting hash that meets the difficulty requirement.
     */
    suspend fun mineNonce(
        data: String,
        difficulty: Int,
        numWorkers: Int = Runtime.getRuntime().availableProcessors()
    ): Pair<Int, String> {
        val targetPrefix = "0".repeat(difficulty)
        val channel = Channel<Pair<Int, String>>()
        coroutineScope {
            repeat(numWorkers) { workerId ->
                launch(Dispatchers.Default) {
                    var nonce = workerId
                    while (true) {
                        val combined = "$data$nonce"
                        val hash = hashString(combined)
                        if (hash.startsWith(targetPrefix)) {
                            channel.send(Pair(nonce, hash)) // Send result to main thread
                            return@launch // Stop this coroutine
                        }
                        nonce += numWorkers
                    }
                }
            }
        }
        return channel.receive()
    }

    private fun hashString(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    companion object {
        fun toModel(entity: LedgerRecordEntity, detached: Boolean = true): PointLedgerRecordModel {
            val model = PointLedgerRecordModel(
                id = UUID.fromString(entity.id),
                userId = UUID.fromString(entity.userId),
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
                    userId = model.userId.toString(),
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
                    transactionNonce = 0,
                    transactionHash = "",
                    createdAt = LocalDateTime.now(),
                )
            }
        }
    }
}

