package uk.co.mulecode.ddd.application.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import uk.co.mulecode.ddd.domain.model.UserFilter
import uk.co.mulecode.ddd.domain.model.UserModel
import uk.co.mulecode.ddd.domain.model.UserStatus
import java.util.UUID

data class UserListDto(
    val users: List<UserDto>,
    val page: Int,
    val totalPages: Int,
    val size: Int,
    val totalElements: Long
)

data class UserDto(
    val id: UUID,
    val name: String,
    val email: String,
    val status: UserStatus?
) {
    companion object {
        @JvmStatic
        fun fromModel(userModel: UserModel): UserDto {
            return UserDto(
                id = userModel.data.id,
                name = userModel.data.name,
                email = userModel.data.email,
                status = userModel.data.status
            )
        }
    }
}

data class UserFilterRequest(
    override val id: UUID? = null,
    override val name: String? = null,
    override val email: String? = null,
    override val status: UserStatus? = null,
) : UserFilter

data class UserRegistrationRequest(
    @field:NotBlank(message = "Name is required")
    @field:Size(min = 5, max = 50, message = "Name must be between 5 and 50 characters")
    val name: String,

    @field:NotBlank(message = "Email is required")
    @field:Size(min = 5, max = 50, message = "Email must be between 5 and 50 characters")
    @field:Email(message = "Invalid email")
    val email: String,

    @field:NotNull(message = "Status is required")
    val status: UserStatus
)

data class UserModifyRequest(
    @field:NotBlank(message = "Name is required")
    @field:Size(min = 5, max = 50, message = "Name must be between 5 and 50 characters")
    val name: String,

    @field:NotBlank(message = "Email is required")
    @field:Size(min = 5, max = 50, message = "Email must be between 5 and 50 characters")
    @field:Email(message = "Invalid email")
    val email: String,

    @field:NotNull(message = "Status is required")
    val status: UserStatus
)
