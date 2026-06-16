package uz.hojiakbar.child_tracking.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.hojiakbar.child_tracking.enums.Activity_Type;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ActivityResponseDto {
    UUID id;
    UUID child_id;
    String child_name;
    Activity_Type type;
    String title;
    String description;
    BigDecimal location_lat;
    BigDecimal location_lon;
    int duration;
    Map<String, Object> metadata;
    Timestamp created_at;
    Timestamp ended_at;
}