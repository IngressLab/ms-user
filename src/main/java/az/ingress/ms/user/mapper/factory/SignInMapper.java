package az.ingress.ms.user.mapper.factory;

import az.ingress.ms.user.dao.entity.UserEntity;
import az.ingress.ms.user.model.response.SignInResponse;

public enum SignInMapper {
    SIGN_IN_MAPPER;

    public SignInResponse buildSignInResponse(UserEntity user) {
        return SignInResponse.of(user.getId(), user.getType());
    }
}
