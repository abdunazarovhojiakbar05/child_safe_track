package uz.hojiakbar.child_tracking.dto.auth;


import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import uz.hojiakbar.child_tracking.dto.request.DeviceRequestDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequestDto {

    @NotBlank(message = "Ism bo'sh bo'lmasligi kerak")
    private String full_name;

    @Column(nullable = false )
    @Email(message = "Email formati noto'g'ri")
    @NotBlank(message = "Email majburiy")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Telefon raqami majburiy")
    @Pattern(regexp = "^\\+998\\d{9}$", message = "Telefon raqami noto'g'ri. Masalan: +998901234567")
    private String phone;



    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Parol kamida 8 ta belgi, 1 ta katta harf, 1 ta raqam va 1 ta maxsus belgidan iborat bo'lishi kerak"
    )
    @Column(nullable = false)
    @Size(min = 8, message = "Parol kamida 8 ta belgi bo'lishi kerak")
    private String password;

}
