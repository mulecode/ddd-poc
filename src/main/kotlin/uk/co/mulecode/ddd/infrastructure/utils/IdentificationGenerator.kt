package uk.co.mulecode.ddd.infrastructure.utils

import com.github.f4b6a3.uuid.UuidCreator
import java.security.SecureRandom
import java.time.Instant
import java.util.UUID

class IdentificationGenerator {
    companion object {

        private val secureRandom = SecureRandom()
        private const val BASE36_ALPHABET = "23456789ABCDEFGHJKLMNPRSTUVWXYZ"

        @JvmStatic
        fun sortedUuid(): UUID {
            return UuidCreator.getTimeOrderedEpoch()
        }

        @JvmStatic
        fun randomBase36Id(size: Int = 10): String {
            val timestamp = Instant.now().epochSecond.toString(36).uppercase()
            val randomPart = (1..size - timestamp.length)
                .map { BASE36_ALPHABET[secureRandom.nextInt(BASE36_ALPHABET.length)] }
                .joinToString("")
            return (timestamp + randomPart).take(size)
        }
    }
}
