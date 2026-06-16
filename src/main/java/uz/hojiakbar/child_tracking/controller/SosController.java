package uz.hojiakbar.child_tracking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.xml.bind.ValidationException;
import lombok.RequiredArgsConstructor;
 import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.hojiakbar.child_tracking.dto.sos.SOSRequestDto;
import uz.hojiakbar.child_tracking.entity.Alerts;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;
import uz.hojiakbar.child_tracking.service.SOSService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sos")
@RequiredArgsConstructor
@Tag(name = "SOS")
public class SosController {

    private final SOSService sosService;

    @PostMapping("/trigger")
    @Operation(summary = "SOS signal yuborish (Child)")
    public ResponseEntity<String> triggerSos(
            @RequestBody SOSRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) throws ValidationException {
        sosService.triggerSos(userDetails, dto.getLatitude(), dto.getLongitude());
        return ResponseEntity.ok("SOS signal yuborildi!");
    }

    @GetMapping("/history")
    @Operation(summary = "SOS tarixini ko'rish")
    public ResponseEntity<List<Alerts>> getSosHistory(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(sosService.getSosHistory(userDetails));
    }

    @PutMapping("/{id}/resolve")
    @Operation(summary = "Alertni hal qilindi deb belgilash (Parent)")
    public ResponseEntity<String> resolve(@PathVariable UUID id) {
        sosService.resolveAlert(id);
        return ResponseEntity.ok("Alert hal qilindi!");
    }
}