package uk.co.mulecode.ddd.domain.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.security.MessageDigest

enum class VerificationStatus {
    PENDING,
    VERIFIED,
    FAILED,
}

interface VerificationVo {
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

    fun create(data: String) {
        val blockChain = runBlocking {
            mineNonce(
                data = "$previousSignature|$data",
                difficulty = difficulty
            )
        }
        verificationCode = blockChain.first
        verificationSignature = blockChain.second
    }

    private suspend fun mineNonce(
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
                    nonce += numWorkers
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
