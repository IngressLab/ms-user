package az.ingress.ms.user.controller

import az.ingress.ms.user.exception.ErrorHandler
import az.ingress.ms.user.model.request.SignInRequest
import az.ingress.ms.user.model.request.SignUpRequest
import az.ingress.ms.user.service.abstraction.UserService
import io.github.benas.randombeans.EnhancedRandomBuilder
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static az.ingress.ms.user.model.enums.UserType.BUYER
import static az.ingress.ms.user.model.response.SignInResponse.of
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class InternalUserControllerTest extends Specification {
    MockMvc mockMvc
    InternalUserController internalUserController
    def random = EnhancedRandomBuilder.aNewEnhancedRandom()
    UserService userService

    void setup() {
        userService = Mock()
        internalUserController = new InternalUserController(userService)
        mockMvc = MockMvcBuilders.standaloneSetup(internalUserController)
                .setControllerAdvice(new ErrorHandler())
                .build()
    }

    def "test signUp()"() {
        given:
        def url = "/internal/v1/users/sign-up"
        def request = random.nextObject(SignUpRequest)
        def content = """
                {
                    "email": "$request.email",
                    "password": "$request.password",
                    "type": "$request.type"
                }
            """

        when:
        mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(status().isCreated())

        then:
        1 * userService.signUp(request)
    }

    def "test signIn()"() {
        given:
        def url = "/internal/v1/users/sign-in"
        def request = random.nextObject(SignInRequest)
        def response = of(123, BUYER)

        def responseAsString = """
         {
          "userId":123,
          "userType": "$response.userType"
         }
            """

        def content = """
         {
            "email": "$request.email",
            "password": "$request.password"
         }
            """


        when:
        def result = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andReturn()

        then:
        1 * userService.signIn(request) >> response
        JSONAssert.assertEquals(responseAsString, result.getResponse().getContentAsString(), true)
    }
}
