package uk.co.mulecode.ddd.interfaces.api

import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.co.mulecode.ddd.application.dto.UserDto
import uk.co.mulecode.ddd.application.dto.UserRegistrationDto
import java.util.concurrent.CompletableFuture

@RestController
@RequestMapping("/users")
@Validated
interface UserApi {

    @PostMapping
    fun registerUser(@Valid @RequestBody userRegistrationDto: UserRegistrationDto): CompletableFuture<UserDto>

    @GetMapping
    fun getAllUsers(): CompletableFuture<List<UserDto>>
}
