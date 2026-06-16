package uz.hojiakbar.child_tracking.dto.parentDto;

import lombok.*;
import uz.hojiakbar.child_tracking.enums.Gender;
import uz.hojiakbar.child_tracking.enums.UserRole;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfileDto {
    private UUID id;
    private String full_name;
    private String email;
    private String phone;
    private String avatar_url;
    private Date date_of_birth;
    private Gender gender;
    private Boolean isActive;
    private LocalDateTime created_at;
}
