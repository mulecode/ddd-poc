package uk.co.mulecode.ddd.interfaces.api

import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.co.mulecode.ddd.application.dto.UserDto
import uk.co.mulecode.ddd.application.dto.UserListDto
import uk.co.mulecode.ddd.application.dto.UserModifyRequest
import uk.co.mulecode.ddd.application.dto.UserRegistrationRequest
import java.util.UUID
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/users")
@Validated
interface UserApi {

    @PostMapping
    fun registerUser(
        @Valid @RequestBody userRegistrationRequest: UserRegistrationRequest
    ): CompletableFuture<UserDto>

    @GetMapping("/{userId}")
    fun getUserById(
        @PathVariable userId: UUID
    ): CompletableFuture<UserDto>

    @PutMapping("/{userId}")
    fun editUser(
        @PathVariable userId: UUID,
        @Valid @RequestBody request: UserModifyRequest
    ): CompletableFuture<UserDto>

    @GetMapping
    fun getAllUsers(
        @PageableDefault(page = 0, size = 10) pageable: Pageable
    ): CompletableFuture<UserListDto>
}
