package uz.hojiakbar.child_tracking.dto.auth;


import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.hojiakbar.child_tracking.entity.Device;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @Email(message = "Email formati noto'g'ri")
    @NotBlank(message = "Email majburiy")
    private String email;

    @Column(length = 6 )
    private String code;

    @Valid
    private Device device;
}
