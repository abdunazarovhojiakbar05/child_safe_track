package uz.hojiakbar.child_tracking.dto.parentDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uz.hojiakbar.child_tracking.enums.Gender;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileDto {

    private String full_name;

    private String phone;

    private String avatar_url;

    private Date date_of_birth;

    private Gender gender;
}