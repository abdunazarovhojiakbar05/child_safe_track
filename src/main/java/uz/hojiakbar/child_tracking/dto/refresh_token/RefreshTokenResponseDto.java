package uz.hojiakbar.child_tracking.dto.refresh_token;


import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenResponseDto {

    @JoinColumn(name = "refresh_token")
    private String refreshToken;

    @JoinColumn(name = "access_token")
    private String accessToken;
    private Long expires_in = 900L;
}
