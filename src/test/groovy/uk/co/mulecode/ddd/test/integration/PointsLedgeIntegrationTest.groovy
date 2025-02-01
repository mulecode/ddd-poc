package uk.co.mulecode.ddd.test.integration


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import uk.co.mulecode.ddd.IntegrationMinTest
import uk.co.mulecode.ddd.application.dto.UserRegistrationDto
import uk.co.mulecode.ddd.application.service.PointLedgerService
import uk.co.mulecode.ddd.application.service.UserService

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class PointsLedgeIntegrationTest extends IntegrationMinTest {

    @Autowired
    UserService userService

    @Autowired
    PointLedgerService pointLedgerService

    def "should return points balance for user id"() {
        given: "A point record for the user"
        def createdUsed = userService.registerUser(new UserRegistrationDto(
                "User Name", "user@email.com"
        ))
        pointLedgerService.initiateLedger(createdUsed.id)

        when: "We call the endpoint to retrieve the user's points balance"
        def result = mockMvc.perform(get("/points/balance")
                .queryParam("userId", createdUsed.id)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()

        then: "The response status is OK and the user points balance is returned"
        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath('$.balance').value(0))
    }

    def "should return error when ledger not found for user"() {
        when: "We call the endpoint to retrieve the user's points balance"
        def result = mockMvc.perform(get("/points/balance")
                .queryParam("userId", "INVALID_USER_ID_TEST")
                .accept(MediaType.APPLICATION_JSON))
                .andReturn()

        then: "The response status is BAD REQUEST and message"
        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath('$.message').value("No points ledger record found for user INVALID_USER_ID_TEST"))
    }

}
