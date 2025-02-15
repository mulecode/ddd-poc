package uk.co.mulecode.ddd.domain.model

enum class VerificationStatus {
    PENDING,
    VERIFIED,
    FAILED,
}

interface VerificationVo {
    val verificationSignature: String
    val verificationCode: Int
    val verificationStatus: VerificationStatus
}

class VerificationnModel {
}
