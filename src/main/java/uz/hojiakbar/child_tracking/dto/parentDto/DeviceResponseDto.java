package uz.hojiakbar.child_tracking.dto.parentDto;

import jakarta.persistence.JoinColumn;
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

    @JoinColumn(name = "os_version")
    String osVersion;
}
