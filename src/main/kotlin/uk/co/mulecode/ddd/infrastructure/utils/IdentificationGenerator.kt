package uk.co.mulecode.ddd.infrastructure.utils

import com.github.f4b6a3.uuid.UuidCreator
import java.util.UUID

class IdentificationGenerator {
    companion object {
        @JvmStatic
        fun sortedUuid(): UUID {
            return UuidCreator.getTimeOrdered()
        }
    }
}
