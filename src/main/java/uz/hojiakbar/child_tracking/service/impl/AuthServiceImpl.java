package uz.hojiakbar.child_tracking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import uz.hojiakbar.child_tracking.config.GlobalVar;
import uz.hojiakbar.child_tracking.dto.auth.*;
import uz.hojiakbar.child_tracking.dto.refresh_token.RefreshTokenRequestDto;
import uz.hojiakbar.child_tracking.dto.refresh_token.RefreshTokenResponseDto;
import uz.hojiakbar.child_tracking.entity.Device;
import uz.hojiakbar.child_tracking.entity.Session;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.enums.Platform;
import uz.hojiakbar.child_tracking.enums.UserRole;
import uz.hojiakbar.child_tracking.exception.ResourceNotFoundException;
import uz.hojiakbar.child_tracking.repository.DeviceRepository;
import uz.hojiakbar.child_tracking.repository.SessionRepository;
import uz.hojiakbar.child_tracking.repository.UsersRepository;
import uz.hojiakbar.child_tracking.service.AuthService;
import uz.hojiakbar.child_tracking.service.RefreshTokenService;
import uz.hojiakbar.child_tracking.util.JwtUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsersRepository usersRepository;
    private final JavaMailSender emailSender;
    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshtokenService;
    private final SessionRepository sessionRepository;
    private final DeviceRepository deviceRepository;


    @Override
    public SendOtpResponse login(SendOtpRequest requestDto) {


        switch (requestDto.getTarget()) {
            case EMAIL -> {
                return getLoginWithEmail(requestDto.getEmail());
            }
            case SMS -> {
                return getLoginWithSMS(requestDto.getEmail());
            }
            case TELEGRAM -> {
                return getLoginWithTelegram(requestDto.getEmail());
            }
            default -> throw new RuntimeException("Not supported target type");
        }


    }

    SendOtpResponse getLoginWithSMS(String email) {
        throw new RuntimeException("SMS login not supported yet");
    }

    SendOtpResponse getLoginWithTelegram(String email) {
        throw new RuntimeException("Telegram login not supported yet");
    }


    SendOtpResponse getLoginWithEmail(String email) {


        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new RuntimeException("Email not valid");
        }

        Users user = usersRepository.findByEmail(email);

        if (user == null) {
            throw new ResourceNotFoundException("Foydalanuvchi topilmadi!");
        }

        Session session = sessionRepository.findSessionByUser_Email(user.getEmail());


        if (session == null) {
            Session session1 = new Session();
            session1.setDeviceId(UUID.fromString(GlobalVar.getDeviceId()));
            session1.setCreatedAt(LocalDateTime.now());
            session1.setExpiresAt(LocalDateTime.now().plusDays(7));
            session1.setIpAddress(GlobalVar.getDeviceName());
            session1.setPlatform(Platform.valueOf(GlobalVar.getPlatform()));
            session1.setAppVersion(GlobalVar.getAppVersion());
            session1.setRevokedAt(null);
            session1.setUser(user);
            sessionRepository.save(session1);
            session = session1;
        }

        Platform platform = Platform.valueOf(GlobalVar.getPlatform());
        UUID deviceID = UUID.fromString(GlobalVar.getDeviceId());
        Device device = deviceRepository.findById(deviceID).orElse(new Device());

        device.setUser(user);
        device.setApp_version(GlobalVar.getAppVersion());
        device.setPlatform(platform);
        device.setDevice_name(GlobalVar.getDeviceName());
        deviceRepository.save(device);


//        if ( GlobalVar.getDeviceId().equals(session.getDeviceId().toString())) {
//            session.setDeviceId(UUID.fromString(GlobalVar.getDeviceId()));
//            sessionRepository.save(session);
//        }
        System.out.println("device Id  :  " + session.getDeviceId().toString());

        if (user == null) {
            throw new ResourceNotFoundException("Foydalanuvchi topilmadi!");
        }


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

    @Transactional
    public LoginResponseDto verifyOtpCode(VerifyOtpRequest dto) {
        LocalDateTime now = LocalDateTime.now();

        Session session1 = sessionRepository.findSessionById(dto.getSessionID());

        if (session1 == null) {
            throw new ResourceNotFoundException("Session topilmadi! Qayta login qiling.");
        }

        Users user = session1.getUser();

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


            return new LoginResponseDto(user, token, refreshToken, expires_in);
        } else {
            throw new RuntimeException("Kod xato!");
        }
    }


    @Transactional
    @Override
    public SendOtpResponse registration(RegistrationRequestDto dto) {


        if (usersRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Bu email allaqachon ro'yxatdan o'tilgan !");
        }

        Users user = new Users();
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setFull_name(dto.getFull_name());
        user.setRole(UserRole.PARENT);
        user.setIsActive(false);
        user.setDate_of_birth(new Date());

        usersRepository.save(user);

        UUID deviceID = UUID.fromString(GlobalVar.getDeviceId());

        Session session = Session.builder()
                .user(user)
                .deviceName(GlobalVar.getDeviceName())
                .platform(Platform.valueOf(GlobalVar.getPlatform()))
                .appVersion(GlobalVar.getAppVersion())
                .deviceId(deviceID)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();
        sessionRepository.save(session);

        return getLoginWithEmail(dto.getEmail());
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
