package az.ingress.ms.user.controller;

import az.ingress.ms.user.model.request.UserProfileUpdateRequest;
import az.ingress.ms.user.model.response.UserResponse;
import az.ingress.ms.user.service.abstraction.UserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static az.ingress.ms.user.model.constants.HeaderConstants.USER_ID;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("v1/users")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @PatchMapping("/profile")
    @ResponseStatus(NO_CONTENT)
    public void updateProfile(@RequestHeader(USER_ID) Long userId,
                              @RequestPart(required = false) MultipartFile profilePicture,
                              @ModelAttribute UserProfileUpdateRequest userProfileUpdateRequest) {
        userService.updateProfile(userId, userProfileUpdateRequest, profilePicture);
    }

    @GetMapping
    public UserResponse getUserById(@RequestHeader(USER_ID) Long userId) {
        return userService.getUserById(userId);
    }
}
