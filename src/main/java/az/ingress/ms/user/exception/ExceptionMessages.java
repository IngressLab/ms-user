package az.ingress.ms.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public enum ExceptionMessages {

    PASSWORD_DOES_NOT_MATCH("Provided password doesn't match"),
    USER_NOT_FOUND_WITH_ID("User not found with ID: %s"),
    USER_NOT_FOUND_WITH_EMAIL("User not found with email: %s"),
    IMAGE_ENCODE_ERROR("Error encoding image"),
    UNEXPECTED_ERROR("Unexpected error occurred");

    String message;
}
