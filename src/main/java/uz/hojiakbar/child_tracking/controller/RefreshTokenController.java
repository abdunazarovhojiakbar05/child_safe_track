package uz.hojiakbar.child_tracking.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.hojiakbar.child_tracking.dto.refresh_token.RefreshTokenRequestDto;
import uz.hojiakbar.child_tracking.dto.refresh_token.RefreshTokenResponseDto;
import uz.hojiakbar.child_tracking.service.AuthService;
import uz.hojiakbar.child_tracking.service.RefreshTokenService;

@RestController
@RequestMapping("/api/v1/auth/refresh")
@RequiredArgsConstructor
public class RefreshTokenController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<RefreshTokenResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto dto){

             return ResponseEntity.ok( authService.refreshToken(dto) ) ;

    }



}
