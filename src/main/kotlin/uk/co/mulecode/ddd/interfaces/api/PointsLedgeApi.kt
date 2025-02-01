package uk.co.mulecode.ddd.interfaces.api

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length
import org.springframework.data.repository.query.Param
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.co.mulecode.ddd.application.dto.PointsLedgeBalanceDto
import uk.co.mulecode.ddd.application.dto.PointsLedgerRecordDto
import java.util.UUID
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/points")
@Validated
interface PointsLedgeApi {
    @PostMapping("/initiate")
    fun initiate(@RequestBody request: PointsInitiateLedgeRequest): CompletableFuture<PointsLedgeBalanceDto>

    @GetMapping("/balance")
    fun balance(@Param("userId") userId: String): CompletableFuture<PointsLedgeBalanceDto>

    @PostMapping("/credit")
    fun credit(@RequestBody request: PointsTransactionRequest): CompletableFuture<PointsLedgeBalanceDto>

    @PostMapping("/debit")
    fun debit(@RequestBody request: PointsTransactionRequest): CompletableFuture<PointsLedgeBalanceDto>

    @GetMapping("/history")
    fun history(
        @Param("userId") userId: String,
        @Param("page") page: Int = 0,
        @Param("batch") batch: Int = 10
    ): CompletableFuture<List<PointsLedgerRecordDto>>
}

data class PointsInitiateLedgeRequest(
    @field:NotBlank
    val userId: String
)

data class PointsTransactionRequest(
    @field:NotBlank
    @field:Length(max = 100)
    val userId: String,

    @field:Min(Int.MIN_VALUE.toLong())
    @field:Max(Int.MAX_VALUE.toLong())
    val points: Int,

    @field:NotBlank
    @field:Length(max = 100)
    val description: String
)
