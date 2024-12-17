package uk.co.mulecode.ddd.application.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import uk.co.mulecode.ddd.application.dto.UserDto
import uk.co.mulecode.ddd.application.dto.UserRegistrationDto
import uk.co.mulecode.ddd.domain.model.UserModel
import uk.co.mulecode.ddd.domain.repository.UserRepository

@Service
class UserService(
    val userRepository: UserRepository
) {

    private val log = KotlinLogging.logger { }

    fun registerUser(userDto: UserRegistrationDto): UserDto {
        val newUserModel = UserModel.createUser(
            name = userDto.name,
            email = userDto.email
        )
        newUserModel.activateUser()
        return userRepository.registerUser(newUserModel)
            .let { UserDto.fromModel(it) }
    }

    fun getAllUsers(): List<UserDto> {
        log.info { "Service: Getting all users ${Thread.currentThread().name}" }
        return userRepository.getAllUsers()
            .map { UserDto.fromModel(it) }
    }
}
