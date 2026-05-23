package uz.hojiakbar.child_tracking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import uz.hojiakbar.child_tracking.entity.Address;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults( level = lombok.AccessLevel.PRIVATE)
public class LocationResponseDto {



    BigDecimal latitude;

    BigDecimal longitude;


    Address address;

    LocalDateTime cratedAt;



}
