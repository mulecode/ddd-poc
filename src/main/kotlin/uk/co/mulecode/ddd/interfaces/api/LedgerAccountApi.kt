package uk.co.mulecode.ddd.interfaces.api

import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.co.mulecode.ddd.application.dto.LedgerAccountCreationDto
import uk.co.mulecode.ddd.application.dto.LedgerAccountDto
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/ledger/accounts")
@Validated
interface LedgerAccountApi {

    @PostMapping
    fun createLedgerAccount(@Valid @RequestBody request: LedgerAccountCreationDto): CompletableFuture<LedgerAccountDto>

    @GetMapping
    fun listAllLedgerAccounts(): CompletableFuture<List<LedgerAccountDto>>
}
