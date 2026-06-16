package uz.hojiakbar.child_tracking.dto.auth;


import jakarta.persistence.JoinColumn;
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

    @JoinColumn(name = "session_id")
    private UUID session_id;


}

