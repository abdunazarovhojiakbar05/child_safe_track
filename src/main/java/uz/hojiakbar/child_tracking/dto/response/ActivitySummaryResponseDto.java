package uz.hojiakbar.child_tracking.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ActivitySummaryResponseDto {

    Double distance ;

    Double placesVisited;

    int screenTimeMin;


}
