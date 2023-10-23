package az.ingress.ms.user.service.concrete;

import az.ingress.ms.user.annotation.Log;
import az.ingress.ms.user.dao.entity.UserEntity;
import az.ingress.ms.user.dao.repository.UserRepository;
import az.ingress.ms.user.exception.NotFoundException;
import az.ingress.ms.user.exception.PasswordMismatchException;
import az.ingress.ms.user.model.request.SignInRequest;
import az.ingress.ms.user.model.request.SignUpRequest;
import az.ingress.ms.user.model.request.UserProfileUpdateRequest;
import az.ingress.ms.user.model.response.SignInResponse;
import az.ingress.ms.user.model.response.UserResponse;
import az.ingress.ms.user.service.abstraction.UserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static az.ingress.ms.user.exception.ExceptionMessages.PASSWORD_DOES_NOT_MATCH;
import static az.ingress.ms.user.exception.ExceptionMessages.USER_NOT_FOUND_WITH_EMAIL;
import static az.ingress.ms.user.exception.ExceptionMessages.USER_NOT_FOUND_WITH_ID;
import static az.ingress.ms.user.mapper.factory.SignInMapper.SIGN_IN_MAPPER;
import static az.ingress.ms.user.mapper.factory.UserMapper.USER_MAPPER;
import static az.ingress.ms.user.util.CommonUtil.COMMON_UTIL;
import static lombok.AccessLevel.PRIVATE;

@Log
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserServiceHandler implements UserService {

    UserRepository userRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        userRepository.save(USER_MAPPER.buildUserEntity(signUpRequest));
    }

    @Override
    public SignInResponse signIn(SignInRequest signInRequest) {
        var user = findUserByEmail(signInRequest.getEmail());
        verifyPassword(signInRequest.getPassword(), user.getPassword());
        return SIGN_IN_MAPPER.buildSignInResponse(user);
    }


    @Override
    public void updateProfile(Long userId, UserProfileUpdateRequest updateRequest, MultipartFile profilePicture) {
        var user = findUserById(userId);
        var base64Image = COMMON_UTIL.convertToBase64(profilePicture);
        USER_MAPPER.updateUserProfile(base64Image, updateRequest, user);
        userRepository.save(user);
    }


    @Override
    public UserResponse getUserById(Long userId) {
        return USER_MAPPER.buildUserResponse(findUserById(userId));
    }

    private UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_WITH_EMAIL.getMessage(), email)));
    }

    private UserEntity findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format(USER_NOT_FOUND_WITH_ID.getMessage(), userId)));
    }

    private void verifyPassword(String rawPassword, String hashedPassword) {
        if (!bCryptPasswordEncoder.matches(rawPassword, hashedPassword)) {
            throw new PasswordMismatchException(PASSWORD_DOES_NOT_MATCH.getMessage());
        }
    }
}