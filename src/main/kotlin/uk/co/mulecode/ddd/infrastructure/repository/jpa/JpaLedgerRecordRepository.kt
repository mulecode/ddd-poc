package uk.co.mulecode.ddd.infrastructure.repository.jpa

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface JpaLedgerRecordRepository : JpaRepository<JpaLedgerRecordEntity, UUID> {

    fun findTopByPayerAccountIdOrderByCreatedDateDesc(accountId: UUID): JpaLedgerRecordEntity

    fun findAllByPayerAccountIdOrderByCreatedDateDesc(accountId: UUID, page: Pageable): Page<JpaLedgerRecordEntity>
}


