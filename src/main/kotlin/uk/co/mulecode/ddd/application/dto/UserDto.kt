package uk.co.mulecode.ddd.application.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import uk.co.mulecode.ddd.domain.model.UserModel

data class UserDto(
    val id: String,
    val name: String,
    val email: String
) {
    companion object {
        @JvmStatic
        fun fromModel(userModel: UserModel): UserDto {
            return UserDto(
                id = userModel.id,
                name = userModel.name,
                email = userModel.email
            )
        }
    }
}

data class UserRegistrationDto(
    @field:NotBlank(message = "Name is required")
    @field:Size(min = 5, max = 50, message = "Name must be between 5 and 50 characters")
    val name: String,
    @field:NotBlank(message = "Email is required")
    @field:Size(min = 5, max = 50, message = "Email must be between 5 and 50 characters")
    @field:Email(message = "Invalid email")
    val email: String
)
