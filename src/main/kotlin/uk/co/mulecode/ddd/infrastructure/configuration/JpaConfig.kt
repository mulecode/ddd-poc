package uk.co.mulecode.ddd.infrastructure.configuration

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["uk.co.mulecode.ddd.infrastructure.repository"])
@EntityScan(basePackages = ["uk.co.mulecode.ddd.infrastructure.repository"])
class JpaConfig
