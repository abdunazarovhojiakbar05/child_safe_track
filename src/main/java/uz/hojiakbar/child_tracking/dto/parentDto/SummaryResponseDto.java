package uz.hojiakbar.child_tracking.dto.parentDto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SummaryResponseDto {

    private Long activeChildren;
    private Long totalAlertsToday;
    private Long unreadAlerts;
}
