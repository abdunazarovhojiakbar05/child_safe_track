package uz.hojiakbar.child_tracking.dto.notification;

import lombok.*;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Users;

import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequest {

    String fcm_token;
    String title;
    String message;
    UUID user_id;
    UUID child_id;
}
