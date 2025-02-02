package uk.co.mulecode.ddd.application.dto

import uk.co.mulecode.ddd.domain.model.PointLedgerRecordModel
import java.math.BigDecimal


data class PointsLedgeBalanceDto(
    val balance: BigDecimal
) {
    companion object {
        fun fromModel(model: PointLedgerRecordModel): PointsLedgeBalanceDto {
            return PointsLedgeBalanceDto(
                balance = model.balanceSnapshot
            )
        }
    }
}

data class PointsLedgerRecordDto(
    val id: String?,
    val points: BigDecimal,
    val balanceAfter: BigDecimal,
    val operation: String,
) {
    companion object {
        @JvmStatic
        fun fromModel(model: PointLedgerRecordModel): PointsLedgerRecordDto {
            return PointsLedgerRecordDto(
                id = model.id.toString(),
                points = model.amount,
                balanceAfter = model.balanceSnapshot,
                operation = model.transactionType.name
            )
        }
    }
}
