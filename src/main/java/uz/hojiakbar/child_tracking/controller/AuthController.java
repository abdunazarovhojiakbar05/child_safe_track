package uz.hojiakbar.child_tracking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.hojiakbar.child_tracking.dto.auth.LoginRequestDto;
import uz.hojiakbar.child_tracking.dto.auth.LoginResponseDto;
import uz.hojiakbar.child_tracking.dto.auth.RegistrationRequestDto;
import uz.hojiakbar.child_tracking.dto.auth.RegistrationResponseDto;
import uz.hojiakbar.child_tracking.dto.refresh_token.RefreshTokenRequestDto;
import uz.hojiakbar.child_tracking.dto.refresh_token.RefreshTokenResponseDto;
import uz.hojiakbar.child_tracking.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autentifikatsiya", description = "Tizimga kirish va ro'yxatdan o'tish API-lari")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Ota-ona login send", description = "Ota-ona tizimga kirishi (kod olishi) uchun faqat email manzilini yuboradi.")
    @PostMapping("/send")
    public ResponseEntity<LoginResponseDto> send(@Valid @RequestBody LoginRequestDto requestDto) {
        return ResponseEntity.ok(authService.login(requestDto));
        ///     TODO  TELEGRAM VA SMS UCHUN ALOHIDA KOD YUBORADIGAN API CHIQARISH KERAK KEYINCHALIK
    }


    @Operation(summary = "Ota-ona login verify", description = "Ota-ona emailga kelgan maxfiy kod bilan emailni yuboradi va tizimga kiradi.")
    @PostMapping("/verify")
    public ResponseEntity<LoginResponseDto> verify(@Valid @RequestBody LoginRequestDto requestDto) {
        return ResponseEntity.ok(authService.login(requestDto));
    }

    @Operation(summary = "Ota-ona ro'yxatdan o'tishi", description = "Yangi ota-ona akkaunti yaratadi va qurilma (device) ma'lumotlarini tizimda saqlaydi.")
    @PostMapping("/registration")
    public ResponseEntity<RegistrationResponseDto> registration(@Valid @RequestBody RegistrationRequestDto requestDto) {
        return ResponseEntity.ok(authService.registration(requestDto));
    }

    @Operation(summary = "Refresh token", description = "Eski yoki muddati tugayotgan refresh token orqali yangi access token va refresh token oladi.")
    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponseDto> refresh(@Valid @RequestBody RefreshTokenRequestDto dto) {
        RefreshTokenResponseDto response = authService.refreshToken(dto);
        /// TODO OPTIMALLASHTIRISH KERAK
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Tizimdan chiqish", description = "Foydalanuvchining faol tokenini bekor qiladi va tizimdan chiqaradi.")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String token) {
        authService.logout(token);
        return ResponseEntity.ok("Tizimdan muvaffaqiyatli chiqildi!");
    }
}