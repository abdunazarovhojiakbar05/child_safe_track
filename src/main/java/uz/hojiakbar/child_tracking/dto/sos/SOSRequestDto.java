package uz.hojiakbar.child_tracking.dto.sos;

import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SOSRequestDto {

    private BigDecimal latitude;
    private BigDecimal longitude;
}
