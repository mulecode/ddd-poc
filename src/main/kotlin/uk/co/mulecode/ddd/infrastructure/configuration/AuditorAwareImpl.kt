package uk.co.mulecode.ddd.infrastructure.configuration

import org.springframework.data.domain.AuditorAware
import java.util.Optional

class AuditorAwareImpl : AuditorAware<String> {

    override fun getCurrentAuditor(): Optional<String> {
        return Optional.of("system")
    }

}
