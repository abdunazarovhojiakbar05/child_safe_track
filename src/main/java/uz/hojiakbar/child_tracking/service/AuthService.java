package uz.hojiakbar.child_tracking.service;

import jakarta.validation.Valid;
import uz.hojiakbar.child_tracking.dto.auth.LoginRequestDto;
import uz.hojiakbar.child_tracking.dto.auth.RegistrationRequestDto;
import uz.hojiakbar.child_tracking.dto.auth.LoginResponseDto;
import uz.hojiakbar.child_tracking.dto.auth.RegistrationResponseDto;
import uz.hojiakbar.child_tracking.dto.refresh_token.RefreshTokenRequestDto;
import uz.hojiakbar.child_tracking.dto.refresh_token.RefreshTokenResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto requestDto);

    RegistrationResponseDto registration(RegistrationRequestDto requestDto);

    RefreshTokenResponseDto refreshToken(@Valid RefreshTokenRequestDto dto);

    void logout(String token);
}
