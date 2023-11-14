package az.ingress.ms.user.controller

import az.ingress.ms.user.exception.ErrorHandler
import az.ingress.ms.user.model.constants.HeaderConstants
import az.ingress.ms.user.model.request.UserProfileUpdateRequest
import az.ingress.ms.user.model.response.UserResponse
import az.ingress.ms.user.service.abstraction.UserService
import io.github.benas.randombeans.EnhancedRandomBuilder
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.RequestPostProcessor
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import static az.ingress.ms.user.model.constants.HeaderConstants.USER_ID
import static az.ingress.ms.user.model.enums.UserType.BUYER
import static org.springframework.http.MediaType.IMAGE_JPEG
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class UserControllerTest extends Specification {
    MockMvc mockMvc
    UserController UserController
    def random = EnhancedRandomBuilder.aNewEnhancedRandom()
    UserService userService

    void setup() {
        userService = Mock()
        UserController = new UserController(userService)
        mockMvc = MockMvcBuilders.standaloneSetup(UserController)
                .setControllerAdvice(new ErrorHandler())
                .build()
    }

    def "test updateProfile()"() {
        given:
        def dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        def url = "/v1/users/profile"
        def userId = random.nextLong()
        def userProfileUpdateRequest = new UserProfileUpdateRequest("Ali", "Nazim", LocalDate.of(1997, 12, 12))
        userProfileUpdateRequest.birthDate = LocalDate.parse("1997-12-12", dateFormatter)
        def profilePictureFile = new MockMultipartFile(
                "profilePicture",
                "test.jpg",
                IMAGE_JPEG.toString(),
                "test".getBytes()
        )

        when:
        mockMvc.perform(multipart(url)
                .with(httpPatch())
                .header(USER_ID, userId)
                .file(profilePictureFile)
                .param("birthDate", userProfileUpdateRequest.birthDate.format(dateFormatter))
                .param("lastName", userProfileUpdateRequest.lastName)
                .param("firstName", userProfileUpdateRequest.firstName)
        ).andExpect(status().isNoContent())

        then:
        1 * userService.updateProfile(userId, userProfileUpdateRequest, profilePictureFile)
    }


    def "test getUserById()"() {
        given:
        def url = "/v1/users"
        def userId = random.nextLong()
        def response = UserResponse.builder()
                .email("test@test.com")
                .userType(BUYER)
                .firstName("Ali")
                .lastName("Nazim")
                .birthDate(LocalDate.of(1997, 12, 12))
                .profilePicture("profilePicture")
                .build()

        def responseAsString = """
            {
                "email": "$response.email",
                "userType": "$response.userType",
                "firstName": "$response.firstName",
                "lastName": "$response.lastName",
                "birthDate": [1997,12,12],
                "profilePicture": "$response.profilePicture"
            }
      """

        when:
        def result = mockMvc.perform(get(url)
                .header(USER_ID, userId))
                .andExpect(status().isOk())
                .andReturn()

        then:
        1 * userService.getUserById(userId) >> response
        JSONAssert.assertEquals(responseAsString, result.getResponse().getContentAsString(), false)
    }

    RequestPostProcessor httpPatch() {
        return new RequestPostProcessor() {
            @Override
            MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PATCH")
                return request
            }
        }
    }
}