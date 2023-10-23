package az.ingress.ms.user.util;

import az.ingress.ms.user.exception.InternalException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static az.ingress.ms.user.exception.ExceptionMessages.IMAGE_ENCODE_ERROR;
import static java.util.Base64.getEncoder;

public enum CommonUtil {
    COMMON_UTIL;

    public String convertToBase64(MultipartFile profilePicture) {
        try {
            return getEncoder().encodeToString(profilePicture.getBytes());
        } catch (IOException e) {
            throw new InternalException(IMAGE_ENCODE_ERROR.getMessage());
        }
    }
}
