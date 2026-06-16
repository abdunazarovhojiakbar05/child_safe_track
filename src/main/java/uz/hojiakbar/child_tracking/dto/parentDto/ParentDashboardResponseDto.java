package uz.hojiakbar.child_tracking.dto.parentDto;

import jakarta.persistence.JoinColumn;
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

    @JoinColumn(name = "daily_activity_summary")
    private List<ActivitySummaryResponseDto> dailyActivitySummary;

    @JoinColumn(name = "recent_alerts")
    private List<AlertResponseDto> recentAlerts;

}
