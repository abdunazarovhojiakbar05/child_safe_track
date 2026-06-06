package uz.hojiakbar.child_tracking.dto.parentDto;


import jakarta.persistence.JoinColumn;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SummaryResponseDto {

    @JoinColumn(name = "active_children")
    private int activeChildren;
    @JoinColumn(name = "total_alerts_today")
    private int totalAlertsToday;
    @JoinColumn(name = "unread_alerts")
    private int unreadAlerts;
}
