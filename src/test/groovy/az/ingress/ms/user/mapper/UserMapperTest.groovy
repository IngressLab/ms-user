package az.ingress.ms.user.mapper

import az.ingress.ms.user.dao.entity.UserEntity
import az.ingress.ms.user.model.request.SignUpRequest
import az.ingress.ms.user.model.request.UserProfileUpdateRequest
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.ingress.ms.user.mapper.factory.UserMapper.USER_MAPPER

class UserMapperTest extends Specification {

    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    def "test buildUserEntity()"() {
        given:
        def signUpRequest = random.nextObject(SignUpRequest)

        when:
        def userEntity = USER_MAPPER.buildUserEntity(signUpRequest)

        then:
        signUpRequest.email == userEntity.email
        signUpRequest.password == userEntity.password
        signUpRequest.type == userEntity.type
    }

    def "test buildUserResponse()"() {
        given:
        def userEntity = random.nextObject(UserEntity)

        when:
        def userResponse = USER_MAPPER.buildUserResponse(userEntity)

        then:
        userEntity.email == userResponse.email
        userEntity.type == userResponse.userType
        userEntity.firstName == userResponse.firstName
        userEntity.lastName == userResponse.lastName
        userEntity.birthDate == userResponse.birthDate
        userResponse.profilePicture == userResponse.profilePicture
    }

    def "test updateUserProfile()"() {
        given:
        def base64Image = random.nextObject(String)
        def updateRequest = random.nextObject(UserProfileUpdateRequest)
        def userEntity = random.nextObject(UserEntity)

        when:
        USER_MAPPER.updateUserProfile(base64Image, updateRequest, userEntity)

        then:
        userEntity.firstName == updateRequest.firstName
        userEntity.lastName == updateRequest.lastName
        userEntity.birthDate == updateRequest.birthDate
        userEntity.profilePicture == base64Image
    }
}