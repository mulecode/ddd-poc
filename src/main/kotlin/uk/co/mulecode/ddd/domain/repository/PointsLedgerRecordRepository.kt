package uk.co.mulecode.ddd.domain.repository

import uk.co.mulecode.ddd.domain.model.PointLedgerRecordModel


interface PointsLedgerRecordRepository {
    fun save(pointLedgerRecordModel: PointLedgerRecordModel): PointLedgerRecordModel
    fun getBalance(userId: String): PointLedgerRecordModel
    fun getHistory(userId: String, page: Int, batch: Int): List<PointLedgerRecordModel>
}
