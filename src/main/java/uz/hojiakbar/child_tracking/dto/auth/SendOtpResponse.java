package uz.hojiakbar.child_tracking.dto.auth;

import jakarta.persistence.JoinColumn;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SendOtpResponse {

    @JoinColumn(name = "session_id")
    UUID sessionId;

    String code;
}
