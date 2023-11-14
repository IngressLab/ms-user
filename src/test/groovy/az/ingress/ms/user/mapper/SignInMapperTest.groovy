package az.ingress.ms.user.mapper

import az.ingress.ms.user.dao.entity.UserEntity
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static az.ingress.ms.user.mapper.factory.SignInMapper.SIGN_IN_MAPPER

class SignInMapperTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()

    def "test buildSignInResponse()"() {
        given:
        def userEntity = random.nextObject(UserEntity)

        when:
        def signInResponse = SIGN_IN_MAPPER.buildSignInResponse(userEntity)

        then:
        signInResponse.userId == userEntity.id
        signInResponse.userType == userEntity.type
    }
}
