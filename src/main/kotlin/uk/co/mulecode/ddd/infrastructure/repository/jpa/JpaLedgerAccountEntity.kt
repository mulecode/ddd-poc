package uk.co.mulecode.ddd.infrastructure.repository.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import uk.co.mulecode.ddd.domain.model.LedgerAccount
import uk.co.mulecode.ddd.domain.model.LedgerAccountStatus
import uk.co.mulecode.ddd.domain.model.LedgerAccountType
import java.util.UUID

@Entity
@Table(name = "ledger_account")
class JpaLedgerAccountEntity(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    override val id: UUID,
    @NotNull
    override val userId: UUID,
    @NotBlank
    @Size(min = 5, max = 50)
    override var name: String,
    @NotBlank
    @Size(min = 5, max = 254)
    override var description: String,
    @Enumerated(EnumType.STRING)
    override var status: LedgerAccountStatus,
    @Enumerated(EnumType.STRING)
    override var accountType: LedgerAccountType,
) : LedgerAccount, JpaAuditingBase()
