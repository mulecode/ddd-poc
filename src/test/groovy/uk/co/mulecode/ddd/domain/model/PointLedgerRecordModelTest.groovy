package uk.co.mulecode.ddd.domain.model

import spock.lang.Specification

class PointLedgerRecordModelTest extends Specification {

    def "Should generate Ledger Record with blockChain"() {
        given:
        def userId = UUID.randomUUID()

        when:
        def ledgerRecord = PointLedgerRecordModel.initiateLedger(userId)

        then:
        assert ledgerRecord.transactionNonce >= 0
        assert ledgerRecord.transactionHash != null
        and: "Ledger chain is valid"
        assert ledgerRecord.isBlockValid()
    }
}
