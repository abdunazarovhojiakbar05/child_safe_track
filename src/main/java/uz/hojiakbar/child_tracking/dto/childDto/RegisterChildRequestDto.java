package uz.hojiakbar.child_tracking.dto.childDto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
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

    @Enumerated(EnumType.STRING)
    Gender gender;

    @NotBlank
    @Email
    String email;

    @Enumerated(EnumType.STRING)
    Status status;

    Date datedOfBirth;

    String phone;


}
