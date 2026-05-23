package uz.hojiakbar.child_tracking.dto.childDto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.hojiakbar.child_tracking.entity.Device;
import uz.hojiakbar.child_tracking.enums.Gender;
import uz.hojiakbar.child_tracking.enums.Status;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RegisterChildRequestDto {

    String inviteCode;

    @Enumerated(EnumType.STRING)
    Gender gender;

    String fullName;

    String password;

    Status status ;

    Date datedOfBirth;

    String phone;

}
