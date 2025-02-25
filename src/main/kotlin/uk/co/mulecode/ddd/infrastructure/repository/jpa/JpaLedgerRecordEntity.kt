package uk.co.mulecode.ddd.infrastructure.repository.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import uk.co.mulecode.ddd.domain.model.LedgerRecord
import uk.co.mulecode.ddd.domain.model.TransactionCategory
import uk.co.mulecode.ddd.domain.model.TransactionType
import uk.co.mulecode.ddd.domain.model.VerificationStatus
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "ledger_record")
class JpaLedgerRecordEntity(
    @Id
    @Column(updatable = false, nullable = false)
    override val id: UUID,
    @Column(updatable = false, nullable = false)
    override val payerAccountId: UUID,
    @Column(updatable = false, nullable = false)
    override val payeeAccountId: UUID,
    @Column(updatable = false, nullable = false)
    override val referenceId: String,
    @Column(updatable = false, nullable = false)
    override val amount: BigDecimal,
    @Column(updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    override val transactionType: TransactionType,
    @Column(updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    override val transactionCategory: TransactionCategory,
    @Column(updatable = false, nullable = false)
    override val balanceSnapshot: BigDecimal,
    @Column(updatable = false, nullable = false)
    override val verificationSignature: String,
    @Column(updatable = false, nullable = false)
    override val verificationCode: Int,
    @Column(updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    override val verificationStatus: VerificationStatus,
) : LedgerRecord, JpaAuditingBase()
