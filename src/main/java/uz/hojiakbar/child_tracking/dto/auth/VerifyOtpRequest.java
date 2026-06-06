package uz.hojiakbar.child_tracking.dto.auth;


import lombok.*;
import uz.hojiakbar.child_tracking.dto.request.DeviceRequestDto;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyOtpRequest {

     private String code;
    private DeviceRequestDto device;
    private UUID sessionID;
}
