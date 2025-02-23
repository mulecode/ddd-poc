package uk.co.mulecode.ddd.interfaces.controller

import org.hamcrest.Matchers
import org.junit.jupiter.api.Tag
import org.spockframework.spring.SpringBean
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import uk.co.mulecode.ddd.ControllerTest
import uk.co.mulecode.ddd.application.dto.UserDto
import uk.co.mulecode.ddd.application.dto.UserListDto
import uk.co.mulecode.ddd.application.dto.UserRegistrationRequest
import uk.co.mulecode.ddd.application.service.UserService
import uk.co.mulecode.ddd.domain.model.UserStatus
import uk.co.mulecode.ddd.interfaces.config.ControllerConfig

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Tag("unit")
@WebMvcTest(UserController)
@Import(ControllerConfig)
class UserControllerTest extends ControllerTest {

    @SpringBean
    private UserService userService = Mock(UserService)

    @Tag("unit")
    def "should return empty users details list when no users registered"() {
        given:
        userService.getAllUsers(_) >> Stub(UserListDto) {
            getUsers() >> []
        }

        when: "We call the endpoint to retrieve the user"
        def result = mockMvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()

        then: "The response status is OK and the user details are returned"
        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.users').isEmpty())
    }

    def "should return users details list"() {
        given: "A user is registered"
        def userId = UUID.randomUUID()
        userService.getAllUsers(_) >> Stub(UserListDto) {
            getUsers() >> [
                    new UserDto(userId, "John Doe", "email@fake.com", UserStatus.ACTIVE)
            ]
        }

        when: "We call the endpoint to retrieve the user"
        def result = mockMvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()

        then: "The response status is OK and the user details are returned"
        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.users[*]').value(
                        Matchers.hasItem(
                                Matchers.anyOf(
                                        Matchers.hasEntry("id", userId),
                                        Matchers.hasEntry("name", "John Doe"),
                                        Matchers.hasEntry("email", "email@fake.com")
                                )
                        )
                ))
    }

    def "Should save a user"() {
        given: "A user registration request"
        def userRegistrationDto = new UserRegistrationRequest(
                "Loren Ipsum",
                "loren@email.com",
                UserStatus.ACTIVE
        )

        and: "Expected identification"
        def userId = UUID.randomUUID()

        and: "a user is registered"
        userService.registerUser(userRegistrationDto) >> Stub(UserDto) {
            getId() >> userId
            getName() >> userRegistrationDto.name
            getEmail() >> userRegistrationDto.email
        }

        when: "We call the endpoint to retrieve the user"
        def result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content('{"name": "Loren Ipsum", "email": "loren@email.com", "status": "ACTIVE"}'))
                .andReturn()

        then: "The response status is OK and the user details are returned"
        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.id').value(userId.toString()))
                .andExpect(jsonPath('$.name').value("Loren Ipsum"))
                .andExpect(jsonPath('$.email').value("loren@email.com"))
    }

}
