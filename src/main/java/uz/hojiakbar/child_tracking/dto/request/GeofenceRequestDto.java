package uz.hojiakbar.child_tracking.dto.request;

import jakarta.persistence.JoinColumn;
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
    @JoinColumn(name = "center_lat")
    private Double centerLat;

    @NotNull
    @JoinColumn(name = "center_lon")
    private Double centerLon;

    @NotNull
    @JoinColumn(name = "radius_metres")
    private Double radiusMetres;

    private Geofences_Type type;

    @JoinColumn(name = "notify_on_enter")
    private Boolean notifyOnEnter = true;

    @JoinColumn(name = "notify_on_exit")
    private Boolean notifyOnExit = true;

}
