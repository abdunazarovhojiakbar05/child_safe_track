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

    private UUID id;

    private String name;

    private Double center_lat;

    private Double center_lon;

    private Double radius_metres;

    private Geofences_Type type;

    private boolean is_active;

    private boolean notify_on_enter;

    private boolean notify_on_exit;
}
