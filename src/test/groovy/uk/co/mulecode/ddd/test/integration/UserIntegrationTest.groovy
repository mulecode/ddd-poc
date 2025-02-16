package uk.co.mulecode.ddd.test.integration

import org.hamcrest.Matchers
import org.springframework.http.MediaType
import uk.co.mulecode.ddd.IntegrationMinTest

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class UserIntegrationTest extends IntegrationMinTest {

    def "Should return empty users details list when no users registered"() {
        when: "We call the endpoint to retrieve the user"
        def result = mockMvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()

        then: "The response status is OK and the user details are returned"
        mockMvc.perform(asyncDispatch(result))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath('$').isEmpty())
    }

    def "Should save a user"() {
        when: "We call the endpoint to retrieve the user"
        def result = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content('{"name": "Loren Ipsum", "email": "loren@email.com"}'))
                .andReturn()

        then: "The response status is OK and the user details are returned"
        mockMvc.perform(asyncDispatch(result))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath('$.id').isNotEmpty())
                .andExpect(jsonPath('$.name').value("Loren Ipsum"))
                .andExpect(jsonPath('$.email').value("loren@email.com"))
    }

    def "Should return users details list"() {
        given: "A user is registered"
        def userRequest = '{"name": "John Doe", "email": "email@fake.com"}'
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userRequest))
                .andExpect(status().isOk())

        when: "We call the endpoint to retrieve the user"
        def result = mockMvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()

        then: "The response status is OK and the user details are returned"
        mockMvc.perform(asyncDispatch(result))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath('$[*]').value(
                        Matchers.hasItem(
                                Matchers.anyOf(
                                        Matchers.hasKey("id"),
                                        Matchers.hasEntry("name", "John Doe"),
                                        Matchers.hasEntry("email", "email@fake.com")
                                )
                        )
                ))
    }
}
