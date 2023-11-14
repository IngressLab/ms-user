package az.ingress.ms.user.service.abstraction;

import az.ingress.ms.user.model.request.SignInRequest;
import az.ingress.ms.user.model.request.SignUpRequest;
import az.ingress.ms.user.model.request.UserProfileUpdateRequest;
import az.ingress.ms.user.model.response.SignInResponse;
import az.ingress.ms.user.model.response.UserResponse;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    void signUp(SignUpRequest signUpRequest);

    SignInResponse signIn(SignInRequest signInRequest);

    void updateProfile(Long userId, UserProfileUpdateRequest userProfileUpdateRequest, MultipartFile profilePicture);

    UserResponse getUserById(Long userId);
}
