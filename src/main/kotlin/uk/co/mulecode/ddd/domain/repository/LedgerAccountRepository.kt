package uk.co.mulecode.ddd.domain.repository

import uk.co.mulecode.ddd.domain.model.LedgerAccountModel
import uk.co.mulecode.ddd.domain.model.LedgerAccountType
import java.util.UUID

interface LedgerAccountRepository {
    fun create(userId: UUID, type: LedgerAccountType, name: String, description: String): LedgerAccountModel
    fun findById(id: UUID): LedgerAccountModel
    fun save(model: LedgerAccountModel): LedgerAccountModel
    fun findAll(): List<LedgerAccountModel>
}
