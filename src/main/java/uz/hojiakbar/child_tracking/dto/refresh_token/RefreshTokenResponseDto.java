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
    private String refresh_token;

    @JoinColumn(name = "access_token")
    private String access_token;

    private Long expires_in = 900L;
}
