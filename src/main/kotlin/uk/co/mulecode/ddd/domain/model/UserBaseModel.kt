package uk.co.mulecode.ddd.domain.model

import uk.co.mulecode.ddd.domain.events.UserActivatedEvent
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

class UserBaseModel(
    val data: User,
) : BaseModel() {

    fun activateUser() {
        data.status = UserStatus.ACTIVE
        addEvent(UserActivatedEvent(this))
    }

}

