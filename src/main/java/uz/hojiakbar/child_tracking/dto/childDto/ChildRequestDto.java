package uz.hojiakbar.child_tracking.dto.childDto;


import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ChildRequestDto {

    String invite_code;


}
