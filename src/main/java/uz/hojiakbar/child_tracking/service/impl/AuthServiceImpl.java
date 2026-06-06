package uz.hojiakbar.child_tracking.service.impl;

 import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
 import org.springframework.http.HttpStatus;
 import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
 import org.springframework.web.server.ResponseStatusException;
 import uz.hojiakbar.child_tracking.dto.auth.*;
import uz.hojiakbar.child_tracking.dto.refresh_token.RefreshTokenRequestDto;
import uz.hojiakbar.child_tracking.dto.refresh_token.RefreshTokenResponseDto;
import uz.hojiakbar.child_tracking.entity.Device;
import uz.hojiakbar.child_tracking.entity.Session;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.enums.Platform;
import uz.hojiakbar.child_tracking.exception.ResourceNotFoundException;
import uz.hojiakbar.child_tracking.repository.DeviceRepository;
import uz.hojiakbar.child_tracking.repository.SessionRepository;
import uz.hojiakbar.child_tracking.repository.UsersRepository;
import uz.hojiakbar.child_tracking.service.AuthService;
import uz.hojiakbar.child_tracking.service.RefreshTokenService;
import uz.hojiakbar.child_tracking.util.JwtUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsersRepository usersRepository;
    private final JavaMailSender emailSender;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final DeviceRepository devicesRepository;
    private final RefreshTokenService refreshtokenService;
    private final SessionRepository sessionRepository;

    @Override
    public SendOtpResponse login(SendOtpRequest requestDto) {


        switch (requestDto.getTarget()) {
            case EMAIL -> {
                return getLoginWithEmail(requestDto);
            }
            case SMS -> {
                return getLoginWithSMS(requestDto);
            }
            case TELEGRAM -> {
                return getLoginWithTelegram(requestDto);
            }

            default -> throw new RuntimeException("Not supported target type");
        }


    }

    SendOtpResponse getLoginWithSMS(SendOtpRequest requestDto) {
        throw new RuntimeException("SMS login not supported yet");
    }

    SendOtpResponse getLoginWithTelegram(SendOtpRequest requestDto) {
        throw new RuntimeException("Telegram login not supported yet");
    }

    SendOtpResponse getLoginWithEmail(@MonotonicNonNull SendOtpRequest requestDto)   {

        String email = requestDto.getEmail();

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new RuntimeException("Email not valid");
        }

        Users user = usersRepository.findByEmail(requestDto.getEmail());

        Session  session = sessionRepository.findSessionByUser_Email(user.getEmail());

/*
        if (user == null) {
            throw new ResourceNotFoundException("Foydalanuvchi topilmadi!");
        }*/


        String code = String.valueOf((int) (Math.random() * 900000) + 100000);

        user.setVerification_code(code);
        user.setCode_generated_at(LocalDateTime.now());
        usersRepository.save(user);

        sendEmail(user.getEmail(), code);

        return SendOtpResponse.builder()
                .sessionId(session.getId())
                .code(code)
                .build();


    }

    public LoginResponseDto verifyOtpCode(VerifyOtpRequest dto) {
        LocalDateTime now = LocalDateTime.now();

        Session session1 = sessionRepository.findSessionById(dto.getSessionID());

        Users user =  session1.getUser();

        if (user == null) {
            throw new ResourceNotFoundException("Foydalanuvchi topilmadi!");
        }

        if (user.getCode_generated_at().plusMinutes(3).isBefore(now)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kod yaroqsiz yoki muddati o'tgan!");
        }


        if (dto.getCode().equals(user.getVerification_code())) {

            String token = jwtUtils.generateToken(user.getEmail());
            String refreshToken = jwtUtils.generateRefreshToken(user.getEmail());

            Date expirationDate = jwtUtils.getExpirationDateFromToken(token);

            long expires_in = (expirationDate != null) ? (expirationDate.getTime() - System.currentTimeMillis()) / 1000 : 86400;

            usersRepository.save(user);


            Session session = sessionRepository.findSessionByUser_Email(user.getEmail());

            session.setAccessToken(token);
            session.setRefreshToken(refreshToken);
            session.setCreatedAt(LocalDateTime.now());
            session.setExpiresAt(LocalDateTime.now().plusDays(7));
            session.setRevokedAt(null);

            sessionRepository.save(session);


            return new LoginResponseDto(new UserDto(user.getId(), user.getFull_name()), token, refreshToken, expires_in, dto.getCode());
        } else {
            throw new RuntimeException("Kod xato!");
        }
    }


    @Override
    public String registration(RegistrationRequestDto dto) {

        if (usersRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Bu email allaqachon ro'yxatdan o'tilgan !");
        }

        Users user = new Users();
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setFull_name(dto.getFull_name());

        user.setIsActive(false);
        user.setPassword_hash(passwordEncoder.encode(dto.getPassword()));
        user.setDate_of_birth(new Date());

        usersRepository.save(user);




        Session session = Session.builder()
                .user(user)
                .ipAddress("unknown")
                .userAgent("unknown")
                .deviceId(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();
        sessionRepository.save(session);

        return user.getFull_name() + " saqlandi!";
    }

    @Async
    public void sendEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("e6cfcee1a42f71");
        message.setTo(to);
        message.setSubject("Login Tasdiqlash Kodi");
        message.setText("Sizning bir martalik kodingiz: " + code);
        emailSender.send(message);
    }

    @Override
    public RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto dto) {
        String refreshToken = dto.getRefreshToken();
        if (refreshToken == null || !jwtUtils.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh token yaroqsiz yoki muddati o'tgan!");
        }

        String email = jwtUtils.getUsernameFromToken(refreshToken);
        Users user = usersRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Foydalanuvchi topilmadi!");
        }


        String newAccessToken = jwtUtils.generateToken(email);
        String newRefreshToken = jwtUtils.generateRefreshToken(email);


        sessionRepository.findByRefreshToken(refreshToken).ifPresent(session -> {
            session.setAccessToken(newAccessToken);
            session.setRefreshToken(newRefreshToken);
            session.setCreatedAt(LocalDateTime.now());
            session.setExpiresAt(LocalDateTime.now().plusDays(7));
            session.setRevokedAt(LocalDateTime.now());

            sessionRepository.save(session);
        });


        Date expirationDate = jwtUtils.getExpirationDateFromToken(newAccessToken);
        long expires_in = (expirationDate != null) ? (expirationDate.getTime() - System.currentTimeMillis()) / 1000 : 900;

        return new RefreshTokenResponseDto(newRefreshToken, newAccessToken, expires_in);
    }

    @Override
    public void logout(String token) {
        if (token != null) {
            refreshtokenService.deleteByAccessToken(token);
        }
    }
}
