package uk.co.mulecode.ddd.interfaces.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor


@Configuration
class ControllerConfig {

    @Bean(name = ["controllerTreadPoolExecutor"])
    fun controllerTreadPoolExecutor(): Executor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 5
        executor.maxPoolSize = 10
        executor.queueCapacity = 25
        executor.setThreadNamePrefix("AppAsync-")
        executor.initialize()
        return executor
    }
}
