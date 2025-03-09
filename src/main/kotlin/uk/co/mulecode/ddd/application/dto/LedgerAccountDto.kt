package uk.co.mulecode.ddd.application.dto

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Digits
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import uk.co.mulecode.ddd.domain.model.LedgerAccountFilter
import uk.co.mulecode.ddd.domain.model.LedgerAccountModel
import uk.co.mulecode.ddd.domain.model.LedgerAccountStatus
import uk.co.mulecode.ddd.domain.model.LedgerAccountType
import uk.co.mulecode.ddd.domain.model.TransactionType
import uk.co.mulecode.ddd.domain.model.VerificationStatus
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class LedgerAccountListDto(
    val ledgerAccounts: List<LedgerAccountDto>,
    val page: Int,
    val totalPages: Int,
    val size: Int,
    val totalElements: Long
)

data class LedgerAccountDto(
    val id: UUID,
    val name: String,
    val description: String,
    val type: LedgerAccountType,
    val status: LedgerAccountStatus,
) {
    companion object {
        @JvmStatic
        fun fromModel(ledgerAccountModel: LedgerAccountModel): LedgerAccountDto {
            return LedgerAccountDto(
                id = ledgerAccountModel.data.id,
                name = ledgerAccountModel.data.name,
                description = ledgerAccountModel.data.description,
                type = ledgerAccountModel.data.accountType,
                status = ledgerAccountModel.data.status
            )
        }
    }
}

data class LedgerAccountDetailsDto(
    val id: UUID,
    val name: String,
    val description: String,
    val type: LedgerAccountType,
    val status: LedgerAccountStatus,
    val balance: BigDecimal,
    val history: List<Record>? = emptyList()
) {
    companion object {

        data class Record(
            val id: UUID,
            val date: Instant,
            val referenceId: String,
            val amount: BigDecimal,
            val transactionType: TransactionType,
            val balanceAfter: BigDecimal,
            val signature: String,
            val previousSignature: String,
            val verificationStatus: VerificationStatus
        )

        @JvmStatic
        fun fromModel(ledgerAccountModel: LedgerAccountModel): LedgerAccountDetailsDto {
            return LedgerAccountDetailsDto(
                id = ledgerAccountModel.data.id,
                name = ledgerAccountModel.data.name,
                description = ledgerAccountModel.data.description,
                type = ledgerAccountModel.data.accountType,
                status = ledgerAccountModel.data.status,
                balance = ledgerAccountModel.balance(),
                history = ledgerAccountModel.history?.map {
                    Record(
                        id = it.data.id,
                        date = it.createdAt,
                        referenceId = it.data.referenceId,
                        amount = it.data.amount,
                        transactionType = it.data.transactionType,
                        balanceAfter = it.data.balanceSnapshot,
                        signature = it.data.verificationSignature,
                        previousSignature = it.previousSignature ?: "",
                        verificationStatus = it.status ?: VerificationStatus.PENDING
                    )
                }
            )
        }
    }
}

data class LedgerAccountFilterRequest(
    override val id: UUID? = null,
    override val name: String? = null,
    override val description: String? = null,
    override val accountType: LedgerAccountType? = null,
    override val status: LedgerAccountStatus? = null,
) : LedgerAccountFilter

data class LedgerAccountCreationDto(
    @field:NotNull(message = "Type is required")
    val type: LedgerAccountType,

    @field:NotBlank(message = "Name is required")
    @field:Size(min = 5, max = 50, message = "Name must be between 5 and 50 characters")
    val name: String,

    @field:NotBlank(message = "Description is required")
    @field:Size(min = 5, max = 254, message = "Description must be between 5 and 254 characters")
    val description: String
)

data class LedgerAccountTransactionCreationDto(
    @field:NotBlank(message = "ReferenceId is required")
    @field:Size(min = 5, max = 50, message = "ReferenceId must be between 5 and 50 characters")
    val referenceId: String,

    @field:DecimalMin(value = "0", inclusive = true) // Prevents negative values
    @field:DecimalMax(value = "9999999.99", inclusive = true) // Upper limit
    @field:Digits(integer = 10, fraction = 2) // Max 10 digits before decimal, 2 after
    val amount: BigDecimal,

    @field:NotNull(message = "TransactionType is required")
    val transactionType: TransactionType,
)
