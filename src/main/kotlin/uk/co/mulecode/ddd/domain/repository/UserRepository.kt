package uk.co.mulecode.ddd.domain.repository

import uk.co.mulecode.ddd.domain.model.UserBaseModel
import java.util.*

interface UserRepository {
    fun create(name: String, email: String): UserBaseModel
    fun findById(userId: UUID): UserBaseModel
    fun save(userModel: UserBaseModel): UserBaseModel
    fun findAll(): List<UserBaseModel>
}
