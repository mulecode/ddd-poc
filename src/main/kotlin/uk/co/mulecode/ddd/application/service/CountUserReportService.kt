package uk.co.mulecode.ddd.application.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import uk.co.mulecode.ddd.domain.repository.ReportCountUserRepository

@Service
class CountUserReportService(
    private val reportCountUserRepository: ReportCountUserRepository
) {

    private val log = KotlinLogging.logger { }

    fun generateCountUserReport() {
        val userCountReport = reportCountUserRepository.getReport()
        log.info { userCountReport.generateReport() }
    }
}
