package az.ingress.ms.user.model.request;

import az.ingress.ms.user.annotation.LogIgnore;
import az.ingress.ms.user.model.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class SignUpRequest {
    String email;

    @LogIgnore
    String password;
    UserType type;
}
