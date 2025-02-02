package uk.co.mulecode.ddd.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.co.mulecode.ddd.application.dto.PointsLedgeBalanceDto
import uk.co.mulecode.ddd.application.dto.PointsLedgerRecordDto
import uk.co.mulecode.ddd.domain.model.PointLedgerRecordModel
import uk.co.mulecode.ddd.domain.repository.PointsLedgerRecordRepository

@Service
class PointLedgerService(
    private val pointsLedgerRecordRepository: PointsLedgerRecordRepository
) {

    @Transactional(readOnly = true)
    fun getBalance(userId: String): PointsLedgeBalanceDto {
        return PointsLedgeBalanceDto.fromModel(pointsLedgerRecordRepository.getBalance(userId))
    }

    @Transactional
    fun initiateLedger(userId: String): PointsLedgeBalanceDto {
        return PointsLedgeBalanceDto.fromModel(
            pointsLedgerRecordRepository.save(
                PointLedgerRecordModel.initiateLedger(userId)
            )
        )
    }

    @Transactional
    fun creditPoints(userId: String, points: Int, description: String): PointsLedgeBalanceDto {
        val userPointBalance = pointsLedgerRecordRepository.getBalance(userId)
        val creditOperation = userPointBalance.credit(points, description)
        return PointsLedgeBalanceDto.fromModel(
            pointsLedgerRecordRepository.save(creditOperation)
        )
    }

    @Transactional
    fun debitPoints(userId: String, points: Int, description: String): PointsLedgeBalanceDto {
        val userPointBalance = pointsLedgerRecordRepository.getBalance(userId)
        val debitOperation = userPointBalance.debit(points, description)
        return PointsLedgeBalanceDto.fromModel(
            pointsLedgerRecordRepository.save(debitOperation)
        )
    }

    @Transactional(readOnly = true)
    fun getHistory(userId: String, page: Int, batch: Int): List<PointsLedgerRecordDto> {
        return pointsLedgerRecordRepository.getHistory(userId, page, batch).map {
            PointsLedgerRecordDto.fromModel(it)
        }
    }
}
