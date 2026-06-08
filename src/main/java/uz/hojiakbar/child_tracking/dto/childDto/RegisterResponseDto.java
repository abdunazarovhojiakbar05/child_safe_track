package uz.hojiakbar.child_tracking.dto.childDto;

import jakarta.persistence.JoinColumn;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RegisterResponseDto {

    UUID child_id;

    UUID parent_id;

    @JoinColumn(name = "access_token")
    String access_token;

    @JoinColumn(name = "refresh_token")
    String refresh_token;

}
