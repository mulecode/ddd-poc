package uk.co.mulecode.ddd.application.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import uk.co.mulecode.ddd.domain.model.LedgerAccountModel
import uk.co.mulecode.ddd.domain.model.LedgerAccountStatus
import uk.co.mulecode.ddd.domain.model.LedgerAccountType
import java.util.UUID

data class LedgerAccountDto(
    val id: UUID,
    val name: String,
    val description: String,
    val type: LedgerAccountType,
    var status: LedgerAccountStatus
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

data class LedgerAccountCreationDto(
    @field:NotNull(message = "User ID is required")
    val userId: UUID,
    @field:NotNull(message = "Type is required")
    val type: LedgerAccountType,
    @field:NotBlank(message = "Name is required")
    @field:Size(min = 5, max = 50, message = "Name must be between 5 and 50 characters")
    val name: String,
    @field:NotBlank(message = "Description is required")
    @field:Size(min = 5, max = 254, message = "Description must be between 5 and 254 characters")
    val description: String
)
