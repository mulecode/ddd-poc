package uk.co.mulecode.ddd.domain.model

class ReportCountUsersModel(
    val count: Long
) {
    /**
     * Generates a report based on data fed into this data domain object
     */
    fun generateReport(): String {
        return "#Report#TotalUsersLog users: $count"
    }
}
