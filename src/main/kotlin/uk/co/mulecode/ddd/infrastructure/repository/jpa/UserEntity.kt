package uk.co.mulecode.ddd.infrastructure.repository.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import uk.co.mulecode.ddd.domain.model.UserStatus

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    val id: String? = null,
    @NotBlank
    @Size(min = 5, max = 50)
    var name: String,
    @NotBlank
    @Email
    @Size(min = 5, max = 50)
    var email: String,
    @Enumerated(EnumType.STRING)
    var status: UserStatus,
    @Version
    @Column(nullable = false)
    val version: Int = 0,
)
