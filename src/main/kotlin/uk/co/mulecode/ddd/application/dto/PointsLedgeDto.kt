package uk.co.mulecode.ddd.application.dto

import uk.co.mulecode.ddd.domain.model.PointLedgerRecordModel
import java.util.*


data class PointsLedgeBalanceDto(
    val balance: Int
) {
    companion object {
        fun fromModel(model: PointLedgerRecordModel): PointsLedgeBalanceDto {
            return PointsLedgeBalanceDto(
                balance = model.balance
            )
        }
    }
}

data class PointsLedgerRecordDto(
    val id: String?,
    val points: Int,
    val balanceAfter: Int,
    val operation: String,
) {
    companion object {
        @JvmStatic
        fun fromModel(model: PointLedgerRecordModel): PointsLedgerRecordDto {
            return PointsLedgerRecordDto(
                id = model.id,
                points = model.points,
                balanceAfter = model.balance,
                operation = model.type.name
            )
        }
    }
}
