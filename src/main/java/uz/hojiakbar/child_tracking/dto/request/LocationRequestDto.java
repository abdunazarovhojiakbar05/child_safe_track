package uz.hojiakbar.child_tracking.dto.request;

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
    private Integer batteryLevel;

    private Boolean isCharging;

    @NotNull
    private Long timestamp;

    private Double accuracy;


}
