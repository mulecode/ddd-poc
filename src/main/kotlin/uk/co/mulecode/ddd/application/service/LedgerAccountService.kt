package uk.co.mulecode.ddd.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.co.mulecode.ddd.application.dto.LedgerAccountCreationDto
import uk.co.mulecode.ddd.application.dto.LedgerAccountDetailsDto
import uk.co.mulecode.ddd.application.dto.LedgerAccountDto
import uk.co.mulecode.ddd.application.dto.LedgerAccountTransactionCreationDto
import uk.co.mulecode.ddd.domain.model.TransactionType
import uk.co.mulecode.ddd.domain.repository.LedgerAccountRepository
import java.math.BigDecimal
import java.util.UUID

@Service
class LedgerAccountService(
    private val ledgerAccountRepository: LedgerAccountRepository
) {

    @Transactional
    fun createLedgerAccount(request: LedgerAccountCreationDto): LedgerAccountDto {
        val account = ledgerAccountRepository.create(
            userId = request.userId,
            type = request.type,
            name = request.name,
            description = request.description
        )
        account.activate()
        account.debit(BigDecimal.ZERO, "Initial balance")
        account.debit(BigDecimal("50"), "Bonus")
        return ledgerAccountRepository.save(account)
            .let { LedgerAccountDto.fromModel(it) }
    }

    @Transactional(readOnly = true)
    fun listAllLedgerAccounts(): List<LedgerAccountDto> {
        return ledgerAccountRepository.findAll()
            .map { LedgerAccountDto.fromModel(it) }
    }

    @Transactional(readOnly = true)
    fun getAccountDetails(accountId: UUID, historySize: Int? = 0): LedgerAccountDetailsDto {
        return ledgerAccountRepository.findById(
            id = accountId,
            historySize = historySize
        ).let { LedgerAccountDetailsDto.fromModel(it) }
    }

    @Transactional
    fun createTransaction(accountId: UUID, request: LedgerAccountTransactionCreationDto): LedgerAccountDetailsDto {
        val account = ledgerAccountRepository.findById(accountId)
        when (request.transactionType) {
            TransactionType.CREDIT -> account.credit(request.amount, request.referenceId)
            TransactionType.DEBIT -> account.debit(request.amount, request.referenceId)
        }
        return ledgerAccountRepository.save(account)
            .let { LedgerAccountDetailsDto.fromModel(it) }
    }
}
