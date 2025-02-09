package uk.co.mulecode.ddd.domain.repository

import uk.co.mulecode.ddd.domain.model.PointLedgerRecordModel
import java.util.*


interface LedgerRepository {
    fun save(pointLedgerRecordModel: PointLedgerRecordModel): PointLedgerRecordModel
    fun getBalance(userId: UUID): PointLedgerRecordModel
    fun getHistory(userId: UUID, page: Int, batch: Int): List<PointLedgerRecordModel>
}
