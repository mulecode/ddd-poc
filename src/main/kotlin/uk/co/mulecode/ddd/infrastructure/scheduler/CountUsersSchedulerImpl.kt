package uk.co.mulecode.ddd.infrastructure.scheduler

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import uk.co.mulecode.ddd.application.schedulers.CountUsersScheduler
import uk.co.mulecode.ddd.application.service.CountUserReportService

@Component
class CountUsersSchedulerImpl(
    private val countUserReportService: CountUserReportService
) : CountUsersScheduler {

    private val log = KotlinLogging.logger { }

    override fun getCountUserReportService(): CountUserReportService {
        return countUserReportService
    }

}
