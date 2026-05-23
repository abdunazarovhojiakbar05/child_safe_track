package uz.hojiakbar.child_tracking.dto.auth;
import lombok.*;
import uz.hojiakbar.child_tracking.enums.UserRole;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationResponseDto {

   private UserDto user;

   private String access_token;

   private String refresh_token;

   private long expires_in;


}
