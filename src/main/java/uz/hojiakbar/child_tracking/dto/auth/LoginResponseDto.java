package uz.hojiakbar.child_tracking.dto.auth;


import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.hojiakbar.child_tracking.entity.Users;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponseDto {

    Users user;

    String token;

    String refresh_token;

    long expires_in;

}
