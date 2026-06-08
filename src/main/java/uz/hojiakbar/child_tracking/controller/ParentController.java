package uz.hojiakbar.child_tracking.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.hojiakbar.child_tracking.dto.childDto.ChildRequestDto;
import uz.hojiakbar.child_tracking.dto.parentDto.ChildDashboardResponseDto;
import uz.hojiakbar.child_tracking.dto.parentDto.ChildListResponseDto;
import uz.hojiakbar.child_tracking.dto.parentDto.ParentDashboardResponseDto;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;
import uz.hojiakbar.child_tracking.service.FamilyRelationsService;
import uz.hojiakbar.child_tracking.service.UsersService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/parent")
@RequiredArgsConstructor
public class ParentController {

    private final UsersService parentService;
    private final FamilyRelationsService relationService;
    private final UsersService usersService;

    @GetMapping
    @Operation(summary = "parent tomon uchun dashboard")
    public ResponseEntity<ParentDashboardResponseDto> getParents(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        return ResponseEntity.ok(parentService.getParentDashboard(email));
    }

    @PostMapping("/add-child")
    @Operation(summary = "bolani qoshayotganda faqatgina ism va email beriladi")
    public ResponseEntity<String> addChild(
            @RequestBody ChildRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String parentEmail = userDetails.getUsername();
        String token = relationService.addChild(requestDto, parentEmail);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/children")
    @Operation(summary = "Ota-onani bolalari ro'yxati")
    public ResponseEntity<List<ChildListResponseDto>> getChildren(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(parentService.getChildrenByParentEmail(userDetails.getUsername()));
    }

    @GetMapping("/child/{childId}")
    @Operation(summary = "Ota-ona id orqali bolasini profilini korishi || bola haqida to'liq ma'lumot")
    public ResponseEntity<ChildDashboardResponseDto> getChildById(
            @PathVariable UUID childId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(parentService.getChildById(childId, userDetails));
    }

    @PutMapping("/fcm-token")
    @Operation(summary = "FCM token yangilash")
    public ResponseEntity<String> updateFcmToken(
            @RequestParam String fcmToken,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Users parent = userDetails.getUsers();
        if (parent == null) {
            throw new RuntimeException("Faqat parent FCM token saqlashi mumkin!");
        }
        parent.setFcmToken(fcmToken);
        usersService.save(parent);
        return ResponseEntity.ok("FCM token saqlandi");
    }
}
