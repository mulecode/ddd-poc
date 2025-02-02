package uk.co.mulecode.ddd.interfaces.api

import jakarta.validation.Valid
import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
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
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/points")
@Validated
interface PointsLedgeApi {
    @PostMapping("/initiate")
    fun initiate(@Valid @RequestBody request: PointsInitiateLedgeRequest): CompletableFuture<PointsLedgeBalanceDto>

    @GetMapping("/balance")
    fun balance(@Param("userId") userId: UUID): CompletableFuture<PointsLedgeBalanceDto>

    @PostMapping("/credit")
    fun credit(@Valid @RequestBody request: PointsTransactionRequest): CompletableFuture<PointsLedgeBalanceDto>

    @PostMapping("/debit")
    fun debit(@Valid @RequestBody request: PointsTransactionRequest): CompletableFuture<PointsLedgeBalanceDto>

    @GetMapping("/history")
    fun history(
        @Param("userId") userId: UUID,
        @Param("page") page: Int = 0,
        @Param("batch") batch: Int = 10
    ): CompletableFuture<List<PointsLedgerRecordDto>>
}

data class PointsInitiateLedgeRequest(
    @field:NotNull
    val userId: UUID
)

data class PointsTransactionRequest(
    @field:NotNull
    val userId: UUID,

    @field:DecimalMin(value = "0", inclusive = true) // Prevents negative values
    @field:DecimalMax(value = "9999999.99", inclusive = true) // Upper limit
    @field:Digits(integer = 10, fraction = 2) // Max 10 digits before decimal, 2 after
    val points: BigDecimal,

    @field:NotBlank
    @field:Length(max = 100)
    val description: String
)
