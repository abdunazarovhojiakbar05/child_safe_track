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
    private Double centerLat;
    private Double centerLon;
    private Double radiusMetres;
    private Geofences_Type type;
    private boolean isActive;
    private boolean notifyOnEnter;
    private boolean notifyOnExit;
}
