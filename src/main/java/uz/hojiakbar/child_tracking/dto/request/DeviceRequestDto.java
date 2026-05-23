package uz.hojiakbar.child_tracking.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.hojiakbar.child_tracking.enums.Platform;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRequestDto {

    private String device_token;

    private Platform platform;

    private String device_model;

    private String os_version;

    private String app_version;
}
