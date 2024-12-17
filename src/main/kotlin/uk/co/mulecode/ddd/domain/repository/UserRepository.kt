package uk.co.mulecode.ddd.domain.repository

import uk.co.mulecode.ddd.domain.model.UserModel

interface UserRepository {
    fun registerUser(userModel: UserModel): UserModel
    fun getAllUsers(): List<UserModel>
}
