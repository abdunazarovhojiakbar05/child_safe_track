package uz.hojiakbar.child_tracking.dto.request;


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

    private String deviceModel;

    private String appVersion;
}
