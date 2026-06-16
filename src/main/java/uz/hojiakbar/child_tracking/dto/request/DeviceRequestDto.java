package uz.hojiakbar.child_tracking.dto.request;


import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.hojiakbar.child_tracking.enums.Platform;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRequestDto {

    private UUID id;

    private String platform;

    @JoinColumn(name = "device_model")
    private String deviceModel;

    @JoinColumn(name = "app_version")
    private String appVersion;
}
