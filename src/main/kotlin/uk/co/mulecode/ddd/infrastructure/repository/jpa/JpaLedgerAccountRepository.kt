package uk.co.mulecode.ddd.infrastructure.repository.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface JpaLedgerAccountRepository : JpaRepository<JpaLedgerAccountEntity, UUID>,
    JpaSpecificationExecutor<JpaLedgerAccountEntity>
