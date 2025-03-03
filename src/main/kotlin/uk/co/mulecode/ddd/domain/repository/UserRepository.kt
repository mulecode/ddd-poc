package uk.co.mulecode.ddd.domain.repository

import org.springframework.data.domain.Pageable
import uk.co.mulecode.ddd.domain.model.UserFilter
import uk.co.mulecode.ddd.domain.model.UserListModel
import uk.co.mulecode.ddd.domain.model.UserModel
import java.util.UUID

interface UserRepository {
    fun findById(userId: UUID): UserModel
    fun save(userModel: UserModel): UserModel
    fun findAll(pageable: Pageable, filter: UserFilter?): UserListModel
}
