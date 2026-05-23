package uz.hojiakbar.child_tracking.dto.childDto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import uz.hojiakbar.child_tracking.entity.Child;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RegisterResponseDto {

    UUID child_id;

    UUID parent_id;

    String accessToken;

    String refreshToken;

}
