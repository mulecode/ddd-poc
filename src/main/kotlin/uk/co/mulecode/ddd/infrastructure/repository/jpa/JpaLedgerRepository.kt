package uk.co.mulecode.ddd.infrastructure.repository.jpa

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface JpaLedgerRepository : JpaRepository<LedgerRecordEntity, String> {

    fun findFirstByUserIdOrderByCreatedAtDesc(userId: UUID): LedgerRecordEntity?

    fun findAllByUserIdOrderByCreatedAtDesc(userId: UUID, page: Pageable): Page<LedgerRecordEntity>
}


