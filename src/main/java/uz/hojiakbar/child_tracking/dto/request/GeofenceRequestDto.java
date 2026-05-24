package uz.hojiakbar.child_tracking.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.hojiakbar.child_tracking.enums.Geofences_Type;

import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GeofenceRequestDto {
    @NotNull
    private UUID childId;

    @NotBlank
    private String name;

    @NotNull
    private Double centerLat;

    @NotNull
    private Double centerLon;

    @NotNull
    private Double radiusMetres;

    private Geofences_Type type; // SAFE, DANGER

    private Boolean notifyOnEnter = true;

    private Boolean notifyOnExit = true;

}
