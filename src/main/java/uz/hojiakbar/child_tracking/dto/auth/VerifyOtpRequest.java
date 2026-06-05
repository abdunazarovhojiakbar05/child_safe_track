package uz.hojiakbar.child_tracking.dto.auth;


import lombok.*;
import uz.hojiakbar.child_tracking.dto.request.DeviceRequestDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyOtpRequest {

    private String email;
    private String code;
    private DeviceRequestDto device;
}
