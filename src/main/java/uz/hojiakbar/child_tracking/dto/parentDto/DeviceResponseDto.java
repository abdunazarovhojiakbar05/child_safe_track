package uz.hojiakbar.child_tracking.dto.parentDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.hojiakbar.child_tracking.enums.Platform;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceResponseDto {

    Platform platform;

    String model;

    String osVersion;
}
