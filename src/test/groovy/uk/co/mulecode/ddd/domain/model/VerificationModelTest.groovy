package uk.co.mulecode.ddd.domain.model

import spock.lang.Specification

class VerificationModelTest extends Specification {

    def "Should match blockchain when same data"() {
        given: "Settings"
        def previousSignature = ""
        def difficulty = 4

        and: "Raw data"
        def rawData = "Some raw data"

        and: "VerificationModel 1"
        def verificationModel1 = new VerificationModel(
                previousSignature, difficulty
        )

        and: "VerificationModel 2"
        def verificationModel2 = new VerificationModel(
                previousSignature, difficulty
        )

        when:
        verificationModel1.create(rawData)
        and:
        verificationModel2.create(rawData)

        then:
        assert verificationModel1.verificationCode == verificationModel2.verificationCode
        assert verificationModel1.verificationSignature == verificationModel2.verificationSignature
    }

//    def "Should chain blocks and validate"() {
//        given: "Settings"
//        def difficulty = 4
//
//        and: "Block 1"
//        def verificationModel1 = new VerificationModel("", difficulty)
//        def rawData1 = "Some raw data 1"
//        verificationModel1.create(rawData1)
//        def code1 = verificationModel1.verificationCode
//        def signature1 = verificationModel1.verificationSignature
//
//        and: "Block 2"
//        def verificationModel2 = new VerificationModel(signature1, difficulty)
//        def rawData2 = "Some raw data 2"
//        verificationModel2.create(rawData2)
//        def code2 = verificationModel2.verificationCode
//        def signature2 = verificationModel2.verificationSignature
//
//        when: "verify Block 1"
//        verificationModel1.verify(code1, signature1)
//        and: "verify Block 2"
//        verificationModel2.verify(code2, signature2)
//
//        then:
//
//    }

}
