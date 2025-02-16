package uk.co.mulecode.ddd.application.service

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Subject
import uk.co.mulecode.ddd.UnitTest
import uk.co.mulecode.ddd.application.dto.UserRegistrationDto
import uk.co.mulecode.ddd.domain.model.User
import uk.co.mulecode.ddd.domain.model.UserBaseModel
import uk.co.mulecode.ddd.domain.model.UserStatus
import uk.co.mulecode.ddd.domain.repository.UserRepository

@SpringBootTest(classes = [
        UserService,
        UserRepository
])
@ActiveProfiles("test")
class UserServiceTest extends UnitTest {

    @Subject
    @Autowired
    UserService userService

    @SpringBean
    UserRepository userRepository = Mock(UserRepository)

    def "Should create a user"() {
        given:
        def userRequest = new UserRegistrationDto("John Doe", "john.doe@example.com")

        // Create real UserBaseModel instance
        def createdModel = Mock(UserBaseModel) {
            getData() >> Stub(User) {
                getId() >> UUID.randomUUID()
                getName() >> userRequest.name
                getEmail() >> userRequest.email
            }
        }

        when:
        def response = userService.registerUser(userRequest)

        then:
        response.id != null
        response.name == userRequest.name
        response.email == userRequest.email

        1 * userRepository.create(userRequest.name, userRequest.email) >> createdModel
        1 * createdModel.activateUser()
        1 * userRepository.save(createdModel) >> createdModel
    }

}
