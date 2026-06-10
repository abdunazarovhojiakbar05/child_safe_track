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
    private int active_children;

    @JoinColumn(name = "total_alerts_today")
    private int total_alerts_today
            ;
    @JoinColumn(name = "unread_alerts")
    private int unread_alerts;
}
