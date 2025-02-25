package uk.co.mulecode.ddd.domain.model

import uk.co.mulecode.ddd.domain.events.UserActivatedEvent
import uk.co.mulecode.ddd.infrastructure.utils.IdentificationGenerator.Companion.sortedUuid
import java.util.UUID

enum class UserStatus {
    ACTIVE,
    INACTIVE
}

interface User {
    val id: UUID
    var name: String
    var email: String
    var status: UserStatus
}

class UserModel(
    val data: User,
    val page: Int? = 0,
    val size: Int? = 10
) : BaseModel() {

    fun activateUser() {
        data.status = UserStatus.ACTIVE
        addEvent(UserActivatedEvent(this))
    }

    companion object {
        fun create(name: String, email: String) = UserModel(
            data = object : User {
                override val id: UUID = sortedUuid()
                override var name: String = name
                override var email: String = email
                override var status: UserStatus = UserStatus.INACTIVE
            }
        )
    }
}

class UserListModel(
    val userList: List<UserModel>,
    val page: Int,
    val totalPages: Int,
    val size: Int,
    val totalElements: Long
)
