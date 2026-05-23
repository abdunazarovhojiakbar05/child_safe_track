package uz.hojiakbar.child_tracking.dto.auth;


import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponseDto {

    UserDto user;

    String token;

    String refresh_token;

    long expires_in;





}
