package az.ingress.ms.user.model.request;

import az.ingress.ms.user.annotation.LogIgnore;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class SignInRequest {
    String email;

    @LogIgnore
    String password;
}
