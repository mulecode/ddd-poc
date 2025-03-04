package uk.co.mulecode.ddd.interfaces.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.domain.Pageable
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import uk.co.mulecode.ddd.application.dto.LedgerAccountCreationDto
import uk.co.mulecode.ddd.application.dto.LedgerAccountDetailsDto
import uk.co.mulecode.ddd.application.dto.LedgerAccountDto
import uk.co.mulecode.ddd.application.dto.LedgerAccountFilterRequest
import uk.co.mulecode.ddd.application.dto.LedgerAccountListDto
import uk.co.mulecode.ddd.application.dto.LedgerAccountTransactionCreationDto
import uk.co.mulecode.ddd.application.service.LedgerAccountService
import uk.co.mulecode.ddd.interfaces.api.AccountDetailsQueryParams
import uk.co.mulecode.ddd.interfaces.api.LedgerAccountApi
import java.util.UUID
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
    override fun listAllLedgerAccounts(
        queryParams: LedgerAccountFilterRequest,
        pageable: Pageable
    ): CompletableFuture<LedgerAccountListDto> {
        return CompletableFuture.completedFuture(
            ledgerAccountService.listAllLedgerAccounts(pageable, queryParams)
        )
    }

    @Async("controllerTreadPoolExecutor")
    override fun getAccountDetails(
        accountId: UUID,
        query: AccountDetailsQueryParams
    ): CompletableFuture<LedgerAccountDetailsDto> {
        return CompletableFuture.completedFuture(
            ledgerAccountService.getAccountDetails(
                accountId = accountId,
                historySize = query.historySize
            )
        )
    }

    @Async("controllerTreadPoolExecutor")
    override fun createLedgerTransaction(
        accountId: UUID,
        request: LedgerAccountTransactionCreationDto
    ): CompletableFuture<LedgerAccountDetailsDto> {
        return CompletableFuture.completedFuture(
            ledgerAccountService.createTransaction(accountId, request)
        )
    }
}
