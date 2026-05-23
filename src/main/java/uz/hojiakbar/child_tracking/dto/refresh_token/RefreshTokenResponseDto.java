package uz.hojiakbar.child_tracking.dto.refresh_token;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenResponseDto {

    private String refreshToken;
    private String accessToken;
    private Long expires_in = 900L;
}
