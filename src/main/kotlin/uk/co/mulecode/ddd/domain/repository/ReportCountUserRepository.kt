package uk.co.mulecode.ddd.domain.repository

import uk.co.mulecode.ddd.domain.model.ReportCountUsersModel

interface ReportCountUserRepository {
    fun getReport(): ReportCountUsersModel
}
