package uz.hojiakbar.child_tracking.controller;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
 import org.springframework.web.bind.annotation.*;
import uz.hojiakbar.child_tracking.dto.auth.EmailDto;
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
    public ResponseEntity<ParentDashboardResponseDto> getParents() {
             /// TODO  DASHBOARDNI YAKUNLASH KERAK  FAQAT DEFAULT MALUMOTLAR BOR
        return ResponseEntity.ok(parentService.getParentDashboard());

    }


    @PostMapping("/generate-invite-code")
    @Operation(summary = "Bola ulanishi uchun 8 xonali kod yaratish")
    public ResponseEntity<String> generateInviteCode(@RequestBody EmailDto email) {
          /// TODO  8 XOANLIK KODNI  TUGASH VAQTI VA BOSHLANISH VAQTINI BERSIH KERAK
        String code = relationService.generateInviteCode(email);

        return ResponseEntity.ok(code);
    }

    @GetMapping("/children")
    @Operation(summary = "Ota-onani bollari ro'yxati")
    public ResponseEntity<List<ChildListResponseDto>> getChildren( @AuthenticationPrincipal CustomUserDetails userDetails) {
        userDetails.getUsername();
         ///   TODO BOLA INVITE KODNI OLGANDA STATUSNI OZGARTISHI VA  TEKSHIRISHI KERAK
        return ResponseEntity.ok(parentService.getChildrenByParentEmail(userDetails.getUsername()));

    }

}
