package uz.hojiakbar.child_tracking.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.hojiakbar.child_tracking.dto.childDto.ChildRequestDto;
import uz.hojiakbar.child_tracking.dto.parentDto.ChildListResponseDto;
import uz.hojiakbar.child_tracking.dto.parentDto.ParentDashboardResponseDto;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;
import uz.hojiakbar.child_tracking.service.FamilyRelationsService;
import uz.hojiakbar.child_tracking.service.UsersService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parent")
@RequiredArgsConstructor
public class ParentController {

    private final UsersService parentService;
    private final FamilyRelationsService relationService;


    @GetMapping
    @Operation(summary = "parent tomon uchun dashboard")
    public ResponseEntity<ParentDashboardResponseDto> getParents(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        /// TODO  DASHBOARDNI YAKUNLASH KERAK  FAQAT DEFAULT MALUMOTLAR BOR
        return ResponseEntity.ok(parentService.getParentDashboard(email));

    }


    @PostMapping("/add-child")
    @Operation(summary = "bolani qoshayotganda faqatgina ism va emaiul beriladi ")
    public ResponseEntity<String> addChild(
            @RequestBody ChildRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        String parentEmail = userDetails.getUsername();
        String token = relationService.generateInviteCode(requestDto, parentEmail);
        return ResponseEntity.ok(token);
    }


    @GetMapping("/children")
    @Operation(summary = "Ota-onani bolalari ro'yxati")
    public ResponseEntity<List<ChildListResponseDto>> getChildren(@AuthenticationPrincipal CustomUserDetails userDetails) {
        ///   TODO BOLA INVITE KODNI OLGANDA STATUSNI OZGARTISHI VA  TEKSHIRISHI KERAK
        return ResponseEntity.ok(parentService.getChildrenByParentEmail(userDetails.getUsername()));

    }


    /// ////////////////////////////////////////////////////////////////////////////////////

    ///     GEOFENCES






}
