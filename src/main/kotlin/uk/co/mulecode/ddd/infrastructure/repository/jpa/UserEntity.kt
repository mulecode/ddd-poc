package uk.co.mulecode.ddd.infrastructure.repository.jpa

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import uk.co.mulecode.ddd.domain.model.UserStatus

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @NotBlank
    @Size(min = 5, max = 50)
    val id: String,
    @NotBlank
    @Size(min = 5, max = 50)
    val name: String,
    @NotBlank
    @Email
    @Size(min = 5, max = 50)
    val email: String,
    @Enumerated(EnumType.STRING)
    val status: UserStatus,
)
