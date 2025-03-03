package uk.co.mulecode.ddd.domain.repository

import org.springframework.data.domain.Pageable
import uk.co.mulecode.ddd.domain.model.LedgerAccountFilter
import uk.co.mulecode.ddd.domain.model.LedgerAccountListModel
import uk.co.mulecode.ddd.domain.model.LedgerAccountModel
import java.util.UUID

interface LedgerAccountRepository {
    fun findById(id: UUID, historySize: Int? = 0): LedgerAccountModel
    fun save(model: LedgerAccountModel): LedgerAccountModel
    fun findAll(pageable: Pageable, filter: LedgerAccountFilter): LedgerAccountListModel
}
