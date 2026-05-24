package uz.hojiakbar.child_tracking.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.hojiakbar.child_tracking.dto.request.GeofenceRequestDto;
import uz.hojiakbar.child_tracking.dto.response.GeofenceResponseDto;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;
import uz.hojiakbar.child_tracking.service.GeofencesService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/geofences")
@RequiredArgsConstructor
public class GeofenceController {

    private final GeofencesService geofenceService;

    @PostMapping("/create")
    public ResponseEntity<GeofenceResponseDto> createGeofence(
            @RequestBody @Valid GeofenceRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        return ResponseEntity.ok(geofenceService.createGeofence(dto, userDetails));
    }

    @GetMapping("/{childId}")
    public ResponseEntity<List<GeofenceResponseDto>> getGeofenceByChildId(
            @PathVariable UUID childId) {

        return ResponseEntity.ok(geofenceService.getGeofencesByChildId(childId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGeofenceById(@PathVariable UUID id) {
        geofenceService.deleteGeofence(id);
        return ResponseEntity.ok("Geofence o'chirildi");
    }
}