package uk.co.mulecode.ddd.application.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.co.mulecode.ddd.application.dto.UserDto
import uk.co.mulecode.ddd.application.dto.UserFilterRequest
import uk.co.mulecode.ddd.application.dto.UserListDto
import uk.co.mulecode.ddd.application.dto.UserModifyRequest
import uk.co.mulecode.ddd.application.dto.UserRegistrationRequest
import uk.co.mulecode.ddd.domain.model.UserModel
import uk.co.mulecode.ddd.domain.model.UserStatus
import uk.co.mulecode.ddd.domain.repository.UserRepository
import java.util.UUID

@Service
class UserService(
    val userRepository: UserRepository
) {

    private val log = KotlinLogging.logger { }

    @Transactional
    fun registerUser(userDto: UserRegistrationRequest): UserDto {
        val newUserModel = UserModel.create(
            name = userDto.name,
            email = userDto.email
        )
        if (userDto.status == UserStatus.ACTIVE) {
            newUserModel.activateUser()
        }
        return userRepository.save(newUserModel)
            .let { UserDto.fromModel(it) }
    }

    @Transactional(readOnly = true)
    fun getUserById(userId: UUID): UserDto {
        return userRepository.findById(userId)
            .let { UserDto.fromModel(it) }
    }

    @Transactional
    fun modifyUser(userId: UUID, userDto: UserModifyRequest): UserDto {
        return userRepository.findById(userId)
            .let {
                it.data.name = userDto.name
                it.data.email = userDto.email
                it.data.status = userDto.status
                userRepository.save(it).let { UserDto.fromModel(it) }
            }
    }

    @Transactional(readOnly = true)
    fun getAllUsers(pageable: Pageable, filter: UserFilterRequest): UserListDto {
        log.info { "Service: Getting all users ${Thread.currentThread().name}" }
        return userRepository.findAll(pageable, filter)
            .let {
                UserListDto(
                    users = it.userList.map { userModel -> UserDto.fromModel(userModel) },
                    page = it.page,
                    totalPages = it.totalPages,
                    size = it.size,
                    totalElements = it.totalElements
                )
            }
    }
}
