package uz.hojiakbar.child_tracking.controller;


 import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Barcha xabarlar", description = "Barcha xabarlar")
    public ResponseEntity<List<Notification>> getAllNotifications(@AuthenticationPrincipal CustomUserDetails userDetails) {
         return ResponseEntity.ok(notificationService.getNotifications(userDetails));
    }

    @GetMapping("/{id}")
    @Operation(summary = " id boyicha Xabarni olish", description = " id boyicha habarni olish  oqilgan deb belgialsh ")
    public ResponseEntity<uz.hojiakbar.child_tracking.entity.Notification> getById(@PathVariable UUID id) {
         return ResponseEntity.ok(notificationService.getById(id));
    }

    @PutMapping("/read-all")
    @Operation(summary = "Hammasini o'qish", description = "Hammasini o'qilgan deb belgilash ")
    public ResponseEntity<String> readAll(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        notificationService.markAllAsRead(userDetails);
         return ResponseEntity.ok("Hammasi o'qildi");
    }

    @PostMapping("/send")
    @Operation(summary = "Xabarni yuborish", description = "Xabar yaratiladi va  yuboriladi ")
    public ResponseEntity<String> sendNotification(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody NotificationRequest request) {
        notificationService.sendNotification(userDetails, request);
        return ResponseEntity.ok("Xabar yuborildi");
    }

}
