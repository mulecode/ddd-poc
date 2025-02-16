package uk.co.mulecode.ddd.interfaces.api

import jakarta.validation.Valid
import jakarta.validation.constraints.Positive
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.co.mulecode.ddd.application.dto.LedgerAccountCreationDto
import uk.co.mulecode.ddd.application.dto.LedgerAccountDetailsDto
import uk.co.mulecode.ddd.application.dto.LedgerAccountDto
import uk.co.mulecode.ddd.application.dto.LedgerAccountTransactionCreationDto
import java.util.UUID
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/ledger/accounts")
@Validated
interface LedgerAccountApi {

    @PostMapping
    fun createLedgerAccount(@Valid @RequestBody request: LedgerAccountCreationDto): CompletableFuture<LedgerAccountDto>

    @GetMapping
    fun listAllLedgerAccounts(): CompletableFuture<List<LedgerAccountDto>>

    @GetMapping("/{accountId}")
    fun getAccountDetails(
        @PathVariable accountId: UUID,
        @Valid @ModelAttribute query: AccountDetailsQueryParams
    ): CompletableFuture<LedgerAccountDetailsDto>

    @PostMapping("/{accountId}/transactions")
    fun createLedgerTransaction(
        @PathVariable accountId: UUID,
        @Valid @RequestBody request: LedgerAccountTransactionCreationDto
    ): CompletableFuture<LedgerAccountDetailsDto>

}

data class AccountDetailsQueryParams(
    @field:Positive(message = "historySize should be a positive number")
    val historySize: Int? = null,
)
