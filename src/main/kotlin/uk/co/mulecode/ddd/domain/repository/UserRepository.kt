package uk.co.mulecode.ddd.domain.repository

import uk.co.mulecode.ddd.domain.model.UserModel
import java.util.*

interface UserRepository {
    fun create(name: String, email: String): UserModel
    fun findById(userId: UUID): UserModel
    fun save(userModel: UserModel): UserModel
    fun findAll(): List<UserModel>
}
