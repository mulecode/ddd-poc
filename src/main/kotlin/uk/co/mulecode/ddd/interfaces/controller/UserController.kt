package uk.co.mulecode.ddd.interfaces.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.domain.Pageable
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import uk.co.mulecode.ddd.application.dto.UserDto
import uk.co.mulecode.ddd.application.dto.UserListDto
import uk.co.mulecode.ddd.application.dto.UserModifyRequest
import uk.co.mulecode.ddd.application.dto.UserRegistrationRequest
import uk.co.mulecode.ddd.application.service.UserService
import uk.co.mulecode.ddd.interfaces.api.UserApi
import java.util.UUID
import java.util.concurrent.CompletableFuture

@Component
class UserController(
    private val userService: UserService
) : UserApi {

    private val log = KotlinLogging.logger { }

    @Async("controllerTreadPoolExecutor")
    override fun registerUser(userRegistrationRequest: UserRegistrationRequest): CompletableFuture<UserDto> {
        return CompletableFuture.completedFuture(
            userService.registerUser(userRegistrationRequest)
        )
    }

    @Async("controllerTreadPoolExecutor")
    override fun getUserById(userId: UUID): CompletableFuture<UserDto> {
        return CompletableFuture.completedFuture(
            userService.getUserById(userId)
        )
    }

    @Async("controllerTreadPoolExecutor")
    override fun editUser(userId: UUID, request: UserModifyRequest): CompletableFuture<UserDto> {
        return CompletableFuture.completedFuture(
            userService.modifyUser(userId, request)
        )
    }

    @Async("controllerTreadPoolExecutor")
    override fun getAllUsers(pageable: Pageable): CompletableFuture<UserListDto> {
        log.info { "controller: Getting all users ${Thread.currentThread().name}" }
        return CompletableFuture.completedFuture(
            userService.getAllUsers(pageable)
        )
    }

}
