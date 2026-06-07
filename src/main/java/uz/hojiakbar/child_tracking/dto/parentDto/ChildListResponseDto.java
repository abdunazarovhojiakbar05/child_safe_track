package uz.hojiakbar.child_tracking.dto.parentDto;


import jakarta.persistence.JoinColumn;
import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.hojiakbar.child_tracking.enums.Status;

import java.util.Date;
 import java.util.UUID;




@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChildListResponseDto {
    UUID id;
    String full_name;
    String phone;
    String avatar_url;
    Date date_of_birth;
    Status verified;
    @JoinColumn(name = "is_active")
    boolean isActive;
    int age;
}
