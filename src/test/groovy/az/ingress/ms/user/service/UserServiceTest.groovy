package az.ingress.ms.user.service

import az.ingress.ms.user.dao.entity.UserEntity
import az.ingress.ms.user.dao.repository.UserRepository
import az.ingress.ms.user.exception.NotFoundException
import az.ingress.ms.user.exception.PasswordMismatchException
import az.ingress.ms.user.model.request.SignInRequest
import az.ingress.ms.user.model.request.SignUpRequest
import az.ingress.ms.user.model.request.UserProfileUpdateRequest
import az.ingress.ms.user.service.abstraction.UserService
import az.ingress.ms.user.service.concrete.UserServiceHandler
import io.github.benas.randombeans.EnhancedRandomBuilder
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import spock.lang.Specification

import static az.ingress.ms.user.mapper.factory.SignInMapper.SIGN_IN_MAPPER
import static az.ingress.ms.user.mapper.factory.UserMapper.USER_MAPPER
import static az.ingress.ms.user.util.CommonUtil.COMMON_UTIL

class UserServiceTest extends Specification {
    def random = EnhancedRandomBuilder.aNewEnhancedRandom()
    UserService userService
    UserRepository userRepository
    BCryptPasswordEncoder bCryptPasswordEncoder

    void setup() {
        userRepository = Mock()
        bCryptPasswordEncoder = Mock()
        userService = new UserServiceHandler(userRepository, bCryptPasswordEncoder)
    }

    def "test signup()"() {
        given:
        def request = random.nextObject(SignUpRequest)
        def userEntity = USER_MAPPER.buildUserEntity(request)

        when:
        userService.signUp(request)

        then:
        1 * userRepository.save(userEntity)
    }

    def "test signIn success case"() {
        given:
        def signInRequest = random.nextObject(SignInRequest)
        def userEntity = random.nextObject(UserEntity)
        def signInResponse = SIGN_IN_MAPPER.buildSignInResponse(userEntity)

        when:
        def response = userService.signIn(signInRequest)

        then:
        1 * userRepository.findByEmail(signInRequest.email) >> Optional.of(userEntity)
        1 * bCryptPasswordEncoder.matches(signInRequest.password, userEntity.password) >> true
        response == signInResponse
    }

    def "test signIn - User Not Found"() {
        given:
        def signInRequest = random.nextObject(SignInRequest)

        when:
        userService.signIn(signInRequest)

        then:
        1 * userRepository.findByEmail(signInRequest.email) >> Optional.empty()
        def exception = thrown(NotFoundException)
        exception.message == "User not found with email: ${signInRequest.email}"
    }

    def "test signIn - Password Mismatch"() {
        given:
        def signInRequest = random.nextObject(SignInRequest)
        def userEntity = random.nextObject(UserEntity)

        when:
        userService.signIn(signInRequest)

        then:
        1 * userRepository.findByEmail(signInRequest.email) >> Optional.of(userEntity)
        1 * bCryptPasswordEncoder.matches(signInRequest.password, userEntity.password)
        def exception = thrown(PasswordMismatchException)
        exception.message == "Provided password doesn't match"
    }

    def "test updateProfile"() {
        given:
        def userId = random.nextLong()
        def updateRequest = random.nextObject(UserProfileUpdateRequest)
        def user = random.nextObject(UserEntity)
        def profilePicture = random.nextObject(MockMultipartFile)
        def base64 = COMMON_UTIL.convertToBase64(profilePicture)
        USER_MAPPER.updateUserProfile(base64, updateRequest, user)

        when:
        userService.updateProfile(userId, updateRequest, profilePicture)

        then:
        1 * userRepository.findById(userId) >> Optional.of(user)
        1 * userRepository.save(user)

        and:
        user.firstName == updateRequest.firstName
        user.lastName == updateRequest.lastName
        user.birthDate == updateRequest.birthDate
        user.profilePicture == base64
    }

    def "test updateProfile - User Not Found"() {
        given:
        def userId = random.nextLong()
        def updateRequest = random.nextObject(UserProfileUpdateRequest)
        def profilePicture = random.nextObject(MockMultipartFile)

        when:
        userService.updateProfile(userId, updateRequest, profilePicture)

        then:
        1 * userRepository.findById(userId) >> Optional.empty()
        def exception = thrown(NotFoundException)
        exception.message == "User not found with ID: ${userId}"
    }

    def "test getUserById"() {
        given:
        def userId = random.nextLong()
        def userEntity = random.nextObject(UserEntity)

        def userResponse = USER_MAPPER.buildUserResponse(userEntity)

        when:
        def result = userService.getUserById(userId)

        then:
        1 * userRepository.findById(userId) >> Optional.of(userEntity)
        result == userResponse
    }
}
