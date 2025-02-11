package uk.co.mulecode.ddd.interfaces.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import uk.co.mulecode.ddd.application.dto.LedgerAccountCreationDto
import uk.co.mulecode.ddd.application.dto.LedgerAccountDto
import uk.co.mulecode.ddd.application.service.LedgerAccountService
import uk.co.mulecode.ddd.interfaces.api.LedgerAccountApi
import java.util.concurrent.CompletableFuture

@Component
class LedgerAccountController(
    private val ledgerAccountService: LedgerAccountService
) : LedgerAccountApi {

    private val log = KotlinLogging.logger { }

    @Async("controllerTreadPoolExecutor")
    override fun createLedgerAccount(request: LedgerAccountCreationDto): CompletableFuture<LedgerAccountDto> {
        return CompletableFuture.completedFuture(
            ledgerAccountService.createLedgerAccount(request)
        )
    }

    @Async("controllerTreadPoolExecutor")
    override fun listAllLedgerAccounts(): CompletableFuture<List<LedgerAccountDto>> {
        return CompletableFuture.completedFuture(
            ledgerAccountService.listAllLedgerAccounts()
        )
    }

}
