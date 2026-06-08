package uz.hojiakbar.child_tracking.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.hojiakbar.child_tracking.dto.request.LocationRequestDto;
import uz.hojiakbar.child_tracking.dto.response.LocationResponseDto;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;
import uz.hojiakbar.child_tracking.service.LocationService;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/location")
@RequiredArgsConstructor
@Tag(name = "Location", description = "Location API")
public class LocationController {

    private final LocationService locationService;

     @PostMapping("/send")
    @Operation(summary = "Child o'z locationini yuboradi")
    public ResponseEntity<String> sendLocation(
            @RequestBody @Valid LocationRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) throws BadRequestException {

        locationService.saveLocation(userDetails, dto);
        return ResponseEntity.ok("Location saqlandi");
    }

     @GetMapping("/last/{childId}")
    @Operation(summary = "Farzandning oxirgi joylashuvi")
    public ResponseEntity<LocationResponseDto> getLastLocation(
            @PathVariable UUID childId) {

        return ResponseEntity.ok(locationService.getLastLocation(childId));
    }
/*
     @GetMapping("/route/{childId}")
    @Operation(summary = "Farzandning yo'l tarixi")
    public ResponseEntity<List<LocationResponseDto>> getRouteHistory(
            @PathVariable UUID childId) {

        return ResponseEntity.ok(locationService.getRouteHistory(childId));
    }*/

    @GetMapping("/route/{childId}")
    @Operation(summary = "Farzandning yo'l tarixi (15 kunlik, sahifalab)")
    public ResponseEntity<Page<LocationResponseDto>> getRouteHistory(
            @PathVariable UUID childId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {

        return ResponseEntity.ok(locationService.getRouteHistory(childId, page, size));
    }
}