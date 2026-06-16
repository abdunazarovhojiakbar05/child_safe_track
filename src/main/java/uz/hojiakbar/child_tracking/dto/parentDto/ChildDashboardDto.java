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

    private String full_name;

    private String avatar_url;

    private boolean is_online;


    private boolean is_charging;

    private LocationResponseDto  last_location;

    private GeofenceResponseDto current_geofence;

    private ActivitySummaryResponseDto daily_activity_summary;
}
