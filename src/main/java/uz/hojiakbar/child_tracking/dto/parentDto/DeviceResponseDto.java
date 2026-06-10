package uz.hojiakbar.child_tracking.dto.parentDto;

import jakarta.persistence.JoinColumn;
import lombok.*;
import uz.hojiakbar.child_tracking.enums.Platform;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeviceResponseDto {

    Platform platform;

    String model;

     String os_version;
}
