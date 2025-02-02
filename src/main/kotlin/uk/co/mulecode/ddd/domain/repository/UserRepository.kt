package uk.co.mulecode.ddd.domain.repository

import uk.co.mulecode.ddd.domain.model.UserModel
import java.util.*

interface UserRepository {
    fun loadUser(userId: UUID): UserModel
    fun registerUser(userModel: UserModel): UserModel
    fun updateUser(userModel: UserModel): UserModel
    fun getAllUsers(): List<UserModel>
}
