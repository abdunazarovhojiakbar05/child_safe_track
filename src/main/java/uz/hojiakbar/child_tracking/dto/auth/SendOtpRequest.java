package uz.hojiakbar.child_tracking.dto.auth;


import lombok.*;
import uz.hojiakbar.child_tracking.dto.request.DeviceRequestDto;
import uz.hojiakbar.child_tracking.enums.OtpTarget;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendOtpRequest {
    private String email;
    private DeviceRequestDto device;
    private OtpTarget target;
 }
