package az.ingress.ms.user.model.response;


import az.ingress.ms.user.annotation.LogIgnore;
import az.ingress.ms.user.model.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    String email;
    UserType userType;
    String firstName;
    String lastName;
    LocalDate birthDate;

    @LogIgnore
    String profilePicture;
}
