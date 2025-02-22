package uk.co.mulecode.ddd.domain.repository

import uk.co.mulecode.ddd.domain.model.UserModel
import java.util.UUID

interface UserRepository {
    fun findById(userId: UUID): UserModel
    fun save(userModel: UserModel): UserModel
    fun findAll(): List<UserModel>
}
