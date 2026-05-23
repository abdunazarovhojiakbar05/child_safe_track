package uz.hojiakbar.child_tracking.dto.parentDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.hojiakbar.child_tracking.dto.response.ActivitySummaryResponseDto;
import uz.hojiakbar.child_tracking.dto.response.AlertResponseDto;
import uz.hojiakbar.child_tracking.dto.response.GeofenceResponseDto;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParentDashboardResponseDto {

    private SummaryResponseDto summary;
    private List<ChildDashboardDto> children;
    private List<GeofenceResponseDto> geofences;
    private List<ActivitySummaryResponseDto> dailyActivitySummary;
    private List<AlertResponseDto> recentAlerts;

}
