package uz.hojiakbar.child_tracking.dto.response;


import jakarta.persistence.JoinColumn;
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

    @JoinColumn(name = "places_visited")
    Double placesVisited;
    @JoinColumn(name = "screen_time_min")
    int screenTimeMin;


}
