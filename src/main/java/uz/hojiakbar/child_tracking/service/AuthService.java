package uz.hojiakbar.child_tracking.service;

import jakarta.validation.Valid;
import uz.hojiakbar.child_tracking.dto.auth.*;
import uz.hojiakbar.child_tracking.dto.refresh_token.RefreshTokenRequestDto;
import uz.hojiakbar.child_tracking.dto.refresh_token.RefreshTokenResponseDto;

public interface AuthService {
    SendOtpResponse login(SendOtpRequest requestDto);

    String registration(RegistrationRequestDto requestDto);

    RefreshTokenResponseDto refreshToken(@Valid RefreshTokenRequestDto dto);

    void logout(String token);

    LoginResponseDto verifyOtpCode(@Valid VerifyOtpRequest requestDto);
}
