package uz.hojiakbar.child_tracking.dto.childDto;

import jakarta.persistence.JoinColumn;
import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Users;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ChildResponseDto {

    Child child;

    @JoinColumn(name = "access_token")
    String accessToken;

    @JoinColumn(name = "refresh_token")
    String refreshToken;

    Users parent;
}
