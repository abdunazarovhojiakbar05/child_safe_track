package uz.hojiakbar.child_tracking.service;

public interface RefreshTokenService {

    String generateRefreshToken(String email);

    void deleteByToken(String token);

    void deleteByAccessToken(String token);
}
