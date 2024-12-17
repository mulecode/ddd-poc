package uk.co.mulecode.ddd.domain.model

import java.security.MessageDigest
import java.time.LocalDateTime
import java.util.*

enum class TransactionType {
    DEBIT,
    CREDIT
}

enum class TransactionStatus {
    PROCESSED,
    PENDING,
    FAILED
}

class PointLedgerRecordModel(
    val id: String,
    val userId: String,
    var points: Int,
    val type: TransactionType,
    var description: String,
    val systemDescription: String,
    val status: TransactionStatus,
    val balance: Int,
    val createdAt: LocalDateTime,
    val transactionHash: String
) {

    fun debit(points: Int, description: String): PointLedgerRecordModel {
        val newBalance = this.balance + points
        val newTransactionHash = calculateHash(points, newBalance, description)
        return PointLedgerRecordModel(
            id = UUID.randomUUID().toString(),
            userId = userId,
            points = points,
            type = TransactionType.DEBIT,
            description = description,
            systemDescription = "Debit of $points points",
            status = TransactionStatus.PROCESSED,
            balance = this.balance + points,
            createdAt = LocalDateTime.now(),
            transactionHash = newTransactionHash
        )
    }

    fun credit(points: Int, description: String): PointLedgerRecordModel {
        val newBalance = this.balance - points
        val newTransactionHash = calculateHash(points, newBalance, description)
        return PointLedgerRecordModel(
            id = UUID.randomUUID().toString(),
            userId = userId,
            points = points,
            type = TransactionType.CREDIT,
            description = description,
            systemDescription = "Credit of $points points",
            status = TransactionStatus.PROCESSED,
            balance = newBalance,
            createdAt = LocalDateTime.now(),
            transactionHash = newTransactionHash
        )
    }

    private fun calculateHash(points: Int, newBalance: Int, newDescription: String): String {
        // Combine current transaction data and originator data into a single string
        val dataPrevious = "|$id|$userId|$balance|$type|$description|$status|$createdAt|:"
        val dataNext = "|$points|$newBalance|$newDescription|"
        // Generate SHA-256 hash
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest("$dataPrevious|$dataNext".toByteArray())
        // Convert to hexadecimal string
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    companion object {
        @JvmStatic
        fun initiateLedger(userId: String): PointLedgerRecordModel {
            val initialBalance = 0
            return PointLedgerRecordModel(
                id = UUID.randomUUID().toString(),
                userId = userId,
                points = 0,
                type = TransactionType.DEBIT,
                description = "Account initiated",
                systemDescription = "Ledger initiated with $initialBalance points",
                status = TransactionStatus.PROCESSED,
                balance = initialBalance,
                createdAt = LocalDateTime.now(),
                transactionHash = ""
            )
        }

    }
}
