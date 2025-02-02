package uk.co.mulecode.ddd.infrastructure.repository.jpa

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JpaPointsLedgerRepository : JpaRepository<PointsLedgerRecordEntity, String> {

    fun findFirstByUserIdOrderByCreatedAtDesc(userId: String): PointsLedgerRecordEntity?

    fun findAllByUserIdOrderByCreatedAtDesc(userId: String, page: Pageable): Page<PointsLedgerRecordEntity>
}


