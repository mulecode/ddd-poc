package uk.co.mulecode.ddd

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Tag

@Tag("unit")
@WebMvcTest
@ActiveProfiles("test")
abstract class ControllerTest extends Specification {

    @Autowired
    WebApplicationContext context

    @Shared
    MockMvc mockMvc

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build()
    }

}
