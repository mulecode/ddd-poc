package uk.co.mulecode.ddd.interfaces.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import uk.co.mulecode.ddd.application.dto.UserDto
import uk.co.mulecode.ddd.application.dto.UserRegistrationDto
import uk.co.mulecode.ddd.application.service.UserService
import uk.co.mulecode.ddd.interfaces.api.UserApi
import java.util.concurrent.CompletableFuture

@Component
class UserController(
    private val userService: UserService
) : UserApi {

    private val log = KotlinLogging.logger { }

    @Async("controllerTreadPoolExecutor")
    override fun registerUser(userRegistrationDto: UserRegistrationDto): CompletableFuture<UserDto> {
        return CompletableFuture.completedFuture(
            userService.registerUser(userRegistrationDto)
        )
    }

    @Async("controllerTreadPoolExecutor")
    override fun getAllUsers(): CompletableFuture<List<UserDto>> {
        log.info { "controller: Getting all users ${Thread.currentThread().name}" }
        return CompletableFuture.completedFuture(
            userService.getAllUsers()
        )
    }

}
