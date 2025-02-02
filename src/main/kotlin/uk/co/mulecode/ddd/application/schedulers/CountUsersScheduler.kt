package uk.co.mulecode.ddd.application.schedulers

import uk.co.mulecode.ddd.application.service.CountUserReportService

interface CountUsersScheduler {

    fun getCountUserReportService(): CountUserReportService

    fun generateReport() {
        getCountUserReportService().generateCountUserReport()
    }
}
