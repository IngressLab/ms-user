package az.ingress.ms.user.model.response;

import az.ingress.ms.user.model.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class SignInResponse {
    Long userId;
    UserType userType;
}
