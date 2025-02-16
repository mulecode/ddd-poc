package uk.co.mulecode.ddd.infrastructure.repository.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import uk.co.mulecode.ddd.domain.model.User
import uk.co.mulecode.ddd.domain.model.UserStatus
import java.util.UUID

@Entity
@Table(name = "user_account")
class JpaUserEntity(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    override val id: UUID,
    @NotBlank
    @Size(min = 5, max = 50)
    override var name: String,
    @NotBlank
    @Email
    @Size(min = 5, max = 50)
    override var email: String,
    @Enumerated(EnumType.STRING)
    override var status: UserStatus,
) : User, JpaAuditingBase()
