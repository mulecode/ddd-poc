package uk.co.mulecode.ddd.domain.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.security.MessageDigest

enum class VerificationStatus {
    PENDING,
    VERIFIED,
    FAILED,
}

interface VerificationDetails {
    val verificationSignature: String
    val verificationCode: Int
    val verificationStatus: VerificationStatus
}

class VerificationModel(
    private val previousSignature: String = "",
    private val difficulty: Int = 4,
) {

    var verificationCode: Int = 0
    var verificationSignature: String = ""
    var verificationStatus: VerificationStatus = VerificationStatus.PENDING

    fun create(data: String) {
        val blockChain = mineNonce(
            data = "$previousSignature|$data",
            difficulty = difficulty
        )
        verificationCode = blockChain.first
        verificationSignature = blockChain.second
        verificationStatus = VerificationStatus.VERIFIED
    }

    private fun mineNonce(
        data: String,
        difficulty: Int
    ): Pair<Int, String> {
        val targetPrefix = "0".repeat(difficulty)
        var nonce = 0
        while (true) {
            val combined = "$data$nonce"
            val hash = hashString(combined)
            if (hash.startsWith(targetPrefix)) {
                return Pair(nonce, hash)
            }
            nonce++
        }
    }

    private suspend fun mineNonce2(
        data: String,
        difficulty: Int,
        numWorkers: Int = Runtime.getRuntime().availableProcessors()
    ): Pair<Int, String> {
        val targetPrefix = "0".repeat(difficulty)
        val channel = Channel<Pair<Int, String>>()
        val parentJob = SupervisorJob()
        val scope = CoroutineScope(Dispatchers.Default + parentJob)

        repeat(numWorkers) { workerId ->
            scope.launch {
                var nonce = workerId
                while (true) {
                    val combined = "$data$nonce"
                    val hash = hashString(combined)
                    if (hash.startsWith(targetPrefix)) {
                        channel.send(Pair(nonce, hash))
                        return@launch
                    }
                    nonce++
                }
            }
        }

        val result = channel.receive()
        parentJob.cancel()
        return result
    }

    private fun hashString(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
