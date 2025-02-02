package uk.co.mulecode.ddd.infrastructure.repository.jpa

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface JpaLedgerRepository : JpaRepository<LedgerRecordEntity, String> {

    fun findFirstByUserIdOrderByCreatedAtDesc(userId: String): LedgerRecordEntity?

    fun findAllByUserIdOrderByCreatedAtDesc(userId: String, page: Pageable): Page<LedgerRecordEntity>
}


