package uz.hojiakbar.child_tracking.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.hojiakbar.child_tracking.dto.childDto.ChildRequestDto;
import uz.hojiakbar.child_tracking.dto.childDto.RegisterChildRequestDto;
import uz.hojiakbar.child_tracking.dto.childDto.RegisterResponseDto;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.service.ChildService;

@RestController
@RequestMapping("/api/v1/child")
@RequiredArgsConstructor
@Tag(name = "Child", description = "Child API")
public class ChildController {

    private final ChildService childService;

    @PostMapping("/join-family")
    @Operation(summary = "Invite kodni tekshirish va ota-onaga ulanish")
    public ResponseEntity<String> joinFamily(@RequestBody ChildRequestDto dto ) {
        return ResponseEntity.ok(childService.verifyCode(dto ));

    }

    @PostMapping("/register")
    @Operation(summary = "Ota-onaga ulanish uchun boshqa ma'lumotlar toliq registratsiya qilish")
    public ResponseEntity<RegisterResponseDto> registerChild(@Valid @RequestBody RegisterChildRequestDto request ) {
        return ResponseEntity.ok(childService.registerChildAndLink(request ));

    }


}
