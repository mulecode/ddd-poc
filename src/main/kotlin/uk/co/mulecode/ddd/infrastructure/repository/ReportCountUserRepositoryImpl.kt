package uk.co.mulecode.ddd.infrastructure.repository

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import uk.co.mulecode.ddd.domain.model.ReportCountUsersModel
import uk.co.mulecode.ddd.domain.repository.ReportCountUserRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaUserRepository

@Component
class ReportCountUserRepositoryImpl(
    private val jpaUserRepository: JpaUserRepository
) : ReportCountUserRepository {

    @Transactional(readOnly = true)
    override fun getReport(): ReportCountUsersModel {
        return ReportCountUsersModel(
            count = jpaUserRepository.count()
        )
    }
}
