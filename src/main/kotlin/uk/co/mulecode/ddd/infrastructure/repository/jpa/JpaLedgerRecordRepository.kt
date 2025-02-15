package uk.co.mulecode.ddd.infrastructure.repository.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface JpaLedgerRecordRepository : JpaRepository<JpaLedgerRecordEntity, UUID> {

    fun findTopByPayerAccountIdOrderByIdDesc(accountId: UUID): JpaLedgerRecordEntity
}


