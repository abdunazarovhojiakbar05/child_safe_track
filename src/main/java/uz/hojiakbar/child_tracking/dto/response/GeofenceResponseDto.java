package uz.hojiakbar.child_tracking.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.hojiakbar.child_tracking.enums.Geofences_Type;

import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class GeofenceResponseDto {
    UUID id;

    String name;

    Geofences_Type type;
}
