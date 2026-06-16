package uz.hojiakbar.child_tracking.dto.response;

import jakarta.persistence.JoinColumn;
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

    Float speed;

    Float accuracy;

    @JoinColumn(name = "battery_level")
    Integer battery_level;

    @JoinColumn(name = "is_charging")
    Boolean is_charging;

    @JoinColumn(name = "recorded_at")
    LocalDateTime recorded_at;

    @JoinColumn(name = "created_at")
    LocalDateTime created_at;

}
