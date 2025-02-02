package uk.co.mulecode.ddd.interfaces.controller

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import uk.co.mulecode.ddd.application.dto.PointsLedgeBalanceDto
import uk.co.mulecode.ddd.application.dto.PointsLedgerRecordDto
import uk.co.mulecode.ddd.application.service.PointLedgerService
import uk.co.mulecode.ddd.interfaces.api.PointsInitiateLedgeRequest
import uk.co.mulecode.ddd.interfaces.api.PointsLedgeApi
import uk.co.mulecode.ddd.interfaces.api.PointsTransactionRequest
import java.util.concurrent.CompletableFuture

@Component
class PointsLedgeController(
    private val service: PointLedgerService
) : PointsLedgeApi {

    @Async("controllerTreadPoolExecutor")
    override fun initiate(request: PointsInitiateLedgeRequest): CompletableFuture<PointsLedgeBalanceDto> {
        return CompletableFuture.completedFuture(
            service.initiateLedger(request.userId)
        )
    }

    @Async("controllerTreadPoolExecutor")
    override fun balance(userId: String): CompletableFuture<PointsLedgeBalanceDto> {
        return CompletableFuture.completedFuture(
            service.getBalance(userId)
        )
    }

    @Async("controllerTreadPoolExecutor")
    override fun credit(request: PointsTransactionRequest): CompletableFuture<PointsLedgeBalanceDto> {
        return CompletableFuture.completedFuture(
            service.creditPoints(request.userId, request.points, request.description)
        )
    }

    @Async("controllerTreadPoolExecutor")
    override fun debit(request: PointsTransactionRequest): CompletableFuture<PointsLedgeBalanceDto> {
        return CompletableFuture.completedFuture(
            service.debitPoints(request.userId, request.points, request.description)
        )
    }

    @Async("controllerTreadPoolExecutor")
    override fun history(userId: String, page: Int, batch: Int): CompletableFuture<List<PointsLedgerRecordDto>> {
        return CompletableFuture.completedFuture(
            service.getHistory(userId, page, batch)
        )
    }
}
