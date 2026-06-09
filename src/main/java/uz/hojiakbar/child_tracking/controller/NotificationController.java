package uz.hojiakbar.child_tracking.controller;


 import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
 import uz.hojiakbar.child_tracking.dto.notification.NotificationRequest;
 import uz.hojiakbar.child_tracking.entity.Notification;
 import uz.hojiakbar.child_tracking.security.CustomUserDetails;
 import uz.hojiakbar.child_tracking.service.NotificationService;

 import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // TODO hamma notificationni olish kerak
        return ResponseEntity.ok(notificationService.getNotifications(userDetails));
    }

    @GetMapping("/{id}")
    public ResponseEntity<uz.hojiakbar.child_tracking.entity.Notification> getById(@PathVariable UUID id) {
        // TODO get boganda is_read truega otishi kerak
        return ResponseEntity.ok(notificationService.getById(id));
    }

    @PutMapping("/read-all")
    public ResponseEntity<String> readAll(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        notificationService.markAllAsRead(userDetails);
        // TODO hamma taskni olib  is_read truega o'tish kerak'
        return ResponseEntity.ok("Hammasi o'qildi");
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody NotificationRequest request) {
        notificationService.sendNotification(userDetails, request);
        return ResponseEntity.ok("Xabar yuborildi");
    }

}
