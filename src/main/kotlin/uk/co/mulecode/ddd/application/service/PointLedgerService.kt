package uk.co.mulecode.ddd.application.service

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.co.mulecode.ddd.application.dto.PointsLedgeBalanceDto
import uk.co.mulecode.ddd.application.dto.PointsLedgerRecordDto
import uk.co.mulecode.ddd.domain.model.PointLedgerRecordModel
import uk.co.mulecode.ddd.domain.repository.LedgerRepository
import java.math.BigDecimal
import java.util.UUID

@Service
class PointLedgerService(
    private val ledgerRepository: LedgerRepository
) {

    private val log = KotlinLogging.logger { }

    @Transactional(readOnly = true)
    fun getBalance(userId: UUID): PointsLedgeBalanceDto {
        log.debug { "Getting balance for user $userId" }
        return PointsLedgeBalanceDto.fromModel(ledgerRepository.getBalance(userId))
    }

    @Transactional
    fun initiateLedger(userId: UUID): PointsLedgeBalanceDto {
        log.debug { "Initiating ledger for user $userId" }
        return PointsLedgeBalanceDto.fromModel(
            ledgerRepository.save(
                PointLedgerRecordModel.initiateLedger(userId)
            )
        )
    }

    @Transactional
    fun creditPoints(userId: UUID, points: BigDecimal, description: String): PointsLedgeBalanceDto {
        log.debug { "Crediting $points points to user $userId" }
        val userPointBalance = ledgerRepository.getBalance(userId)
        val creditOperation = userPointBalance.credit(points, description)
        return PointsLedgeBalanceDto.fromModel(
            ledgerRepository.save(creditOperation)
        )
    }

    @Transactional
    fun debitPoints(userId: UUID, points: BigDecimal, description: String): PointsLedgeBalanceDto {
        log.debug { "Debiting $points points from user $userId" }
        val userPointBalance = ledgerRepository.getBalance(userId)
        val debitOperation = userPointBalance.debit(points, description)
        return PointsLedgeBalanceDto.fromModel(
            ledgerRepository.save(debitOperation)
        )
    }

    @Transactional(readOnly = true)
    fun getHistory(userId: UUID, page: Int, batch: Int): List<PointsLedgerRecordDto> {
        log.debug { "Getting history for user $userId" }
        return ledgerRepository.getHistory(userId, page, batch).map {
            PointsLedgerRecordDto.fromModel(it)
        }
    }
}
