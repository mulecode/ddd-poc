package uk.co.mulecode.ddd.application.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.co.mulecode.ddd.application.dto.LedgerAccountCreationDto
import uk.co.mulecode.ddd.application.dto.LedgerAccountDto
import uk.co.mulecode.ddd.domain.repository.LedgerAccountRepository

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
        return ledgerAccountRepository.save(account)
            .let { LedgerAccountDto.fromModel(it) }
    }

    @Transactional(readOnly = true)
    fun listAllLedgerAccounts(): List<LedgerAccountDto> {
        return ledgerAccountRepository.findAll()
            .map { LedgerAccountDto.fromModel(it) }
    }
}
