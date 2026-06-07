package uz.hojiakbar.child_tracking.dto.request;

import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationRequestDto {

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private Double speed;

    @Min(0) @Max(100)
    @JoinColumn(name = "battery_level")
    private Integer batteryLevel;

    @JoinColumn(name = "is_charging")
    private Boolean isCharging;

    @NotNull
    private Long timestamp;

    private Double accuracy;


}
