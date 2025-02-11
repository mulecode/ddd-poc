package uk.co.mulecode.ddd.application.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.co.mulecode.ddd.application.dto.UserDto
import uk.co.mulecode.ddd.application.dto.UserRegistrationDto
import uk.co.mulecode.ddd.domain.repository.UserRepository

@Service
class UserService(
    val userRepository: UserRepository
) {

    private val log = KotlinLogging.logger { }

    @Transactional
    fun registerUser(userDto: UserRegistrationDto): UserDto {
        val newUserModel = userRepository.create(
            name = userDto.name,
            email = userDto.email
        )
        newUserModel.activateUser()
        return userRepository.save(newUserModel)
            .let { UserDto.fromModel(it) }
    }

    @Transactional(readOnly = true)
    fun getAllUsers(): List<UserDto> {
        log.info { "Service: Getting all users ${Thread.currentThread().name}" }
        return userRepository.findAll()
            .map { UserDto.fromModel(it) }
    }
}
