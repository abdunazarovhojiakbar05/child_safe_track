package uz.hojiakbar.child_tracking.dto.parentDto;

import lombok.*;
import uz.hojiakbar.child_tracking.dto.response.ActivitySummaryResponseDto;
import uz.hojiakbar.child_tracking.dto.response.GeofenceResponseDto;
import uz.hojiakbar.child_tracking.dto.response.LocationResponseDto;

import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChildDashboardDto {

    private UUID id;

    private String fullName;

    private String avatarUrl;

    private boolean isOnline;

    private Integer batteryLevel;

    private boolean isCharging;

    private LocationResponseDto  lastLocation;

    private GeofenceResponseDto currentGeofence;

    private ActivitySummaryResponseDto dailyActivitySummary;
}
