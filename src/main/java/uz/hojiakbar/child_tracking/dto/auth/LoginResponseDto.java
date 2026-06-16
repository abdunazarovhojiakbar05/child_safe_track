package uz.hojiakbar.child_tracking.dto.auth;


import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.enums.Status;
import uz.hojiakbar.child_tracking.enums.UserRole;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponseDto {

     UUID userId;

     String email;

     UserRole role;

     String full_name;

     String avatar_url;

     String phone;

     String fcm_token;

     Status status;

     Boolean is_active;

     Date date_of_birth;

    String token;

    String refresh_token;

    long expires_in;

}
