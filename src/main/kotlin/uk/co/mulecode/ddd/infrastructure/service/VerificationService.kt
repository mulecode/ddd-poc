package uk.co.mulecode.ddd.infrastructure.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service
import java.security.MessageDigest


data class CreateVerificationResponse(
    val verificationCode: Int,
    val verificationSignature: String,
)


interface VerificationService {
    fun difficulty(): Int
    fun create(previousSignature: String, data: String): CreateVerificationResponse

}

@Service
class VerificationServiceImpl : VerificationService {

    override fun difficulty(): Int {
        return 4
    }

    override fun create(previousSignature: String, data: String): CreateVerificationResponse {

        val blockChain = runBlocking {
            mineNonce(
                data = "$previousSignature|$data",
                difficulty = difficulty()
            )
        }

        return CreateVerificationResponse(
            verificationCode = blockChain.first,
            verificationSignature = blockChain.second,
        )
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
