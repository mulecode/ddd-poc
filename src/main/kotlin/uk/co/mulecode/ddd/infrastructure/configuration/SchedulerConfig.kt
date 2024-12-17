package uk.co.mulecode.ddd.infrastructure.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.annotation.EnableScheduling

// Enable scheduling for the application but not in test profile

@Configuration
@EnableScheduling
@Profile("!test")
class SchedulerConfig

