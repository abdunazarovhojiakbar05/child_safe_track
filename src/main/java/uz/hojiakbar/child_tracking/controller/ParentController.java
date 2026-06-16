package uz.hojiakbar.child_tracking.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.hojiakbar.child_tracking.dto.childDto.ChildRequestDto;
import uz.hojiakbar.child_tracking.dto.parentDto.*;
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


    @GetMapping("/data")
    @Operation(summary = "Parent va bolalar ma'lumotlari")
    public ResponseEntity<ParentData> getParentData(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(parentService.getParentData(userDetails.getUsername()));
    }



    @PostMapping("/manage_child")
    @Operation(summary = "bolani qoshayotganda faqatgina ism va email beriladi")
    public ResponseEntity<String>  manage_child (
            @RequestBody ChildRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String parentEmail = userDetails.getUsername();
        String token = relationService.addChild(requestDto, parentEmail);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/children")
    @Operation(summary = "Ota-onani bolalari ro'yxati")
    public ResponseEntity<List<ChildListResponseDto>> get_children(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(parentService.getChildrenByParentEmail(userDetails.getUsername()));
    }

    @GetMapping("/child/{childId}")
    @Operation(summary = "Ota-ona id orqali bolasini profilini korishi || bola haqida to'liq ma'lumot")
    public ResponseEntity<ChildDashboardResponseDto> get_child_by_id(
            @PathVariable UUID childId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(parentService.getChildById(childId, userDetails));
    }

    @PutMapping("/fcm_token")
    @Operation(summary = "FCM token yangilash")
    public ResponseEntity<String> update_fcm_token(
            @RequestBody FCMTokenRequest fcmToken,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Users parent = userDetails.getUsers();
        if (parent == null) {
            throw new RuntimeException("Faqat parent FCM token saqlashi mumkin!");
        }

        parent.setFcm_token(fcmToken.getFcm_token());
        usersService.save(parent);
        return ResponseEntity.ok("FCM token saqlandi");
    }



    @GetMapping("/profile")
    @Operation(summary = "Parent profili")
    public ResponseEntity<UserProfileDto> get_profile(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(parentService.getProfile(userDetails));
    }

    @PutMapping("/profile")
    @Operation(summary = "Profilni tahrirlash")
    public ResponseEntity<UserProfileDto> update_profile(
            @RequestBody UpdateProfileDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(parentService.updateProfile(dto, userDetails));
    }
}
