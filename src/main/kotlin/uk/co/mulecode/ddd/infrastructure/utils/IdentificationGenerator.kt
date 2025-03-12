package uk.co.mulecode.ddd.infrastructure.utils

import com.github.f4b6a3.uuid.UuidCreator
import java.security.SecureRandom
import java.time.Instant
import java.util.UUID

class IdentificationGenerator {
    companion object {

        private val secureRandom = SecureRandom()
        private val CUSTOM_BASE32_ALPHABET = "23456789ABCDEFGHJKLMNPRSTUVWXYZ"

        @JvmStatic
        fun sortedUuid(): UUID {
            return UuidCreator.getTimeOrderedEpoch()
        }

        @JvmStatic
        fun randomBase32Id(size: Int = 17): String {
            val now = Instant.now()
            val timestamp = toCustomBase32(now.epochSecond * 1_000 + now.nano / 1_000_000)
            val randomPart = (1..size - timestamp.length)
                .map { CUSTOM_BASE32_ALPHABET[secureRandom.nextInt(CUSTOM_BASE32_ALPHABET.length)] }
                .joinToString("")
            return timestamp + randomPart
        }

        private fun toCustomBase32(value: Long): String {
            var num = value
            val result = StringBuilder()
            while (num > 0) {
                result.append(CUSTOM_BASE32_ALPHABET[(num % 32).toInt()])
                num /= 32
            }
            return result.reverse().toString()
        }
    }
}
