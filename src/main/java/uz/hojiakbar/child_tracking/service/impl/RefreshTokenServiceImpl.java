package uz.hojiakbar.child_tracking.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.hojiakbar.child_tracking.repository.SessionRepository;
import uz.hojiakbar.child_tracking.service.RefreshTokenService;
import uz.hojiakbar.child_tracking.util.JwtUtils;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final JwtUtils jwtUtils;
    private final SessionRepository sessionRepository;

    @Override
    public String generateRefreshToken(String email) {
        return jwtUtils.generateRefreshToken(email);
    }

    @Override
    public void deleteByToken(String token) {
        sessionRepository.deleteByRefreshToken(token);
    }

    @Override
    public void deleteByAccessToken(String token) {

    }
}
