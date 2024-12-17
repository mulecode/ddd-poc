package uk.co.mulecode.ddd.infrastructure.repository.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version
import java.time.LocalDateTime

@Entity
@Table(name = "points_ledger_records")
class PointsLedgerRecordEntity(
    @Id
    val id: String,
    @Column(nullable = false)
    val userId: String,
    @Column(nullable = false)
    val points: Int,
    @Column(nullable = false)
    val transactionType: String,
    @Column(nullable = true)
    val description: String,
    @Column(nullable = false)
    val systemDescription: String,
    @Column(nullable = false)
    val transactionStatus: String,
    @Column(nullable = false)
    val balance: Int,
    @Version
    @Column(nullable = false)
    val version: Int = 0,
    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(nullable = false)
    val transactionHash: String,
)
