package uk.co.mulecode.ddd.application.service

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Subject
import uk.co.mulecode.ddd.UnitTest
import uk.co.mulecode.ddd.domain.model.TransactionCategory
import uk.co.mulecode.ddd.domain.model.TransactionStatus
import uk.co.mulecode.ddd.domain.model.TransactionType
import uk.co.mulecode.ddd.infrastructure.repository.LedgerRepositoryImpl
import uk.co.mulecode.ddd.infrastructure.repository.jpa.JpaLedgerRepository
import uk.co.mulecode.ddd.infrastructure.repository.jpa.LedgerRecordEntity

@SpringBootTest(classes = [
        PointLedgerService,
        LedgerRepositoryImpl
])
@ActiveProfiles("test")
class PointLedgerServiceTest extends UnitTest {

    @Subject
    @Autowired
    PointLedgerService pointLedgerService

    @SpringBean
    JpaLedgerRepository jpaPointsLedgerRepository = Mock(JpaLedgerRepository)

    def "should create a point ledger"() {
        given:
        def userId = UUID.randomUUID()

        when:
        pointLedgerService.initiateLedger(userId)

        then:
        1 * jpaPointsLedgerRepository.save({
            def entity = it as LedgerRecordEntity
            assert entity.id != null
            assert entity.userId == userId.toString()
            assert entity.payerAccountId == null
            assert entity.payeeAccountId == null
            assert entity.linkedTransactionId == null
            assert entity.referenceId == "Account initiated"
            assert entity.amount == BigDecimal.ZERO
            assert entity.transactionType == TransactionType.CREDIT
            assert entity.transactionCategory == TransactionCategory.STANDARD
            assert entity.balanceSnapshot == BigDecimal.ZERO
            assert entity.transactionStatus == TransactionStatus.PROCESSED
            assert entity.metadata == "{}"
            assert entity.transactionNonce == 0
            assert entity.transactionHash == ""
            assert entity.createdAt != null
            return entity
        }) >> { args -> args[0] }
    }
}
