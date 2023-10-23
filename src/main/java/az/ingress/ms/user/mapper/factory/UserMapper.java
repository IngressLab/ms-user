package az.ingress.ms.user.mapper.factory;

import az.ingress.ms.user.dao.entity.UserEntity;
import az.ingress.ms.user.model.request.SignUpRequest;
import az.ingress.ms.user.model.request.UserProfileUpdateRequest;
import az.ingress.ms.user.model.response.UserResponse;

import java.time.LocalDate;

import static java.util.Optional.ofNullable;

public enum UserMapper {
    USER_MAPPER;

    public UserEntity buildUserEntity(SignUpRequest signUpRequest) {
        return UserEntity.builder()
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .type(signUpRequest.getType())
                .build();
    }

    public UserResponse buildUserResponse(UserEntity userEntity) {
        return UserResponse.builder()
                .email(userEntity.getEmail())
                .userType(userEntity.getType())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .birthDate(userEntity.getBirthDate())
                .profilePicture(userEntity.getProfilePicture())
                .build();
    }

    public void updateUserProfile(String base64Image, UserProfileUpdateRequest updateRequest, UserEntity user) {
        var updateRequestOptional = ofNullable(updateRequest);
        updateRequestOptional.map(UserProfileUpdateRequest::getFirstName).ifPresent(user::setFirstName);
        updateRequestOptional.map(UserProfileUpdateRequest::getLastName).ifPresent(user::setLastName);
        updateRequestOptional.map(UserProfileUpdateRequest::getBirthDate).ifPresent(user::setBirthDate);
        ofNullable(base64Image).ifPresent(user::setProfilePicture);
    }
}
