package uk.co.mulecode.ddd.infrastructure.repository.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version
import uk.co.mulecode.ddd.domain.model.TransactionCategory
import uk.co.mulecode.ddd.domain.model.TransactionStatus
import uk.co.mulecode.ddd.domain.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime


@Entity
@Table(name = "ledger")
class LedgerRecordEntity(
    // Identity fields
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    val id: String,
    @Column(name = "user_id", updatable = false, nullable = false)
    val userId: String,
    @Column(nullable = true)
    val payerAccountId: String?,
    @Column(nullable = true)
    val payeeAccountId: String?,
    @Column(name = "linked_transaction_id", updatable = false, nullable = true)
    val linkedTransactionId: String?,
    @Column(nullable = true)
    var referenceId: String,

    // Transaction fields
    @Column(nullable = false)
    val amount: BigDecimal,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val transactionType: TransactionType,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val transactionCategory: TransactionCategory,
    @Column(nullable = false)
    val balanceSnapshot: BigDecimal,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val transactionStatus: TransactionStatus,

    // Metadata fields generic values
    @Column(nullable = false)
    var metadata: String,

    // Blockchain fields
    @Column(nullable = false)
    val transactionNonce: Int,
    @Column(nullable = false, unique = true)
    val transactionHash: String,

    // Audit fields
    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    // Optimistic locking
    @Version
    @Column(nullable = false)
    val version: Int = 0,
)
