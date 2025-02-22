package uk.co.mulecode.ddd.domain.repository

import uk.co.mulecode.ddd.domain.model.LedgerAccountModel
import java.util.UUID

interface LedgerAccountRepository {
    fun findById(id: UUID, historySize: Int? = 0): LedgerAccountModel
    fun save(model: LedgerAccountModel): LedgerAccountModel
    fun findAll(): List<LedgerAccountModel>
}
