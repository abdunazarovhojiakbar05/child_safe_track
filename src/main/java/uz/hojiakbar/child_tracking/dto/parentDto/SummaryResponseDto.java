package uz.hojiakbar.child_tracking.dto.parentDto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SummaryResponseDto {

    private int activeChildren;
    private int totalAlertsToday;
    private int unreadAlerts;
}
