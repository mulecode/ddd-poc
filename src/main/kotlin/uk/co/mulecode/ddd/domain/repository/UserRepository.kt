package uk.co.mulecode.ddd.domain.repository

import uk.co.mulecode.ddd.domain.model.UserModel

interface UserRepository {
    fun loadUser(userId: String): UserModel
    fun registerUser(userModel: UserModel): UserModel
    fun updateUser(userModel: UserModel): UserModel
    fun getAllUsers(): List<UserModel>
}
