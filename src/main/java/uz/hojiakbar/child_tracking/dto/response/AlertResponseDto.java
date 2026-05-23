package uz.hojiakbar.child_tracking.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.hojiakbar.child_tracking.enums.Alert_Severity;
import uz.hojiakbar.child_tracking.enums.Alert_Type;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AlertResponseDto {

    UUID id;

    Alert_Type type;

    Alert_Severity severity;

    String title;

    LocalDateTime created_at;
}
