package uz.hojiakbar.child_tracking.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.hojiakbar.child_tracking.dto.response.ActivityResponseDto;
import uz.hojiakbar.child_tracking.entity.Activities;
import uz.hojiakbar.child_tracking.enums.Activity_Type;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;
import uz.hojiakbar.child_tracking.service.ActivitiesService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivitiesController {

    private final ActivitiesService activitiesService;

    @GetMapping("/{childId}")
    public ResponseEntity<List<ActivityResponseDto>> getByChild(
            @PathVariable UUID childId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(activitiesService.getByChild(childId, userDetails));
    }

    @GetMapping("/{childId}/filter")
    public ResponseEntity<List<ActivityResponseDto>> getByChildAndType(
            @PathVariable UUID childId,
            @RequestParam Activity_Type type,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(activitiesService.getByChildAndType(childId, type, userDetails));
    }
}