package uk.co.mulecode.ddd.application.service

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Subject
import uk.co.mulecode.ddd.UnitTest
import uk.co.mulecode.ddd.domain.model.PointLedgerRecordModel
import uk.co.mulecode.ddd.infrastructure.repository.PointsLedgerRecordRepositoryImpl
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaPointsLedgerRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.PointsLedgerRecordEntity

@SpringBootTest(classes = [
        PointLedgerService,
        PointsLedgerRecordRepositoryImpl
])
@ActiveProfiles("test")
class PointLedgerServiceTest extends UnitTest {

    @Subject
    @Autowired
    PointLedgerService pointLedgerService

    @SpringBean
    JpaPointsLedgerRepository jpaPointsLedgerRepository = Mock(JpaPointsLedgerRepository)

    def "should create a point ledger"() {
        given:
        def userId = "UserId_v"
        def expectedCreated = PointLedgerRecordModel.initiateLedger(userId)

        when:
        pointLedgerService.initiateLedger("UserId_v")

        then:
        1 * jpaPointsLedgerRepository.save({
            def entity = it as PointsLedgerRecordEntity
            assert entity.id != null
            assert entity.userId == expectedCreated.userId
            assert entity.points == expectedCreated.points
            assert entity.balance == expectedCreated.balance
            assert entity.transactionType == expectedCreated.type.name()
            assert entity.description == expectedCreated.description
            assert entity.systemDescription == expectedCreated.systemDescription
            assert entity.transactionStatus == expectedCreated.status.name()
            return entity
        }) >> { args -> args[0] }
    }
}
