package uz.hojiakbar.child_tracking.controller;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.xml.bind.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.hojiakbar.child_tracking.entity.Alerts;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;
import uz.hojiakbar.child_tracking.service.AlertService;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/alert")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertsService;

    @GetMapping("/timeline/{childId}")
    @Operation(summary = "Bolaning kunlik faoliyati")
    public ResponseEntity<List<Alerts>> getTimeline(
            @PathVariable UUID childId,
            @RequestParam(required = false) LocalDate date) {
        return ResponseEntity.ok(alertsService.getTimeline(childId, date));
    }

    @GetMapping("/history/{childId}")
    @Operation(summary = "Bolaning alert tarixi")
    public ResponseEntity<List<Alerts>> getHistory(
            @PathVariable UUID childId) {
        return ResponseEntity.ok(alertsService.getHistory(childId));
    }

    @GetMapping("/unread")
    @Operation(summary = "O'qilmagan alertlar (Parent)")
    public ResponseEntity<List<Alerts>> getUnread(
            @AuthenticationPrincipal CustomUserDetails userDetails) throws ValidationException {
        return ResponseEntity.ok(alertsService.getUnread(userDetails));
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "Alertni o'qilgan deb belgilash")
    public ResponseEntity<String> markAsRead(@PathVariable UUID id) {
        alertsService.markAsRead(id);
        return ResponseEntity.ok("O'qildi!");
    }

    @PutMapping("/read-all")
    @Operation(summary = "Hammasini o'qilgan deb belgilash")
    public ResponseEntity<String> markAllAsRead(
            @AuthenticationPrincipal CustomUserDetails userDetails) throws ValidationException {
        alertsService.markAllAsRead(userDetails);
        return ResponseEntity.ok("Hammasi o'qildi!");
    }
}
