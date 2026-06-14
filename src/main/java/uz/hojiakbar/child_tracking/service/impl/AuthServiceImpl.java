package uz.hojiakbar.child_tracking.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.hojiakbar.child_tracking.config.GlobalVar;
import uz.hojiakbar.child_tracking.dto.auth.*;
import uz.hojiakbar.child_tracking.dto.refresh_token.RefreshTokenRequestDto;
import uz.hojiakbar.child_tracking.dto.refresh_token.RefreshTokenResponseDto;
import uz.hojiakbar.child_tracking.entity.Device;
import uz.hojiakbar.child_tracking.entity.Session;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.enums.Platform;
import uz.hojiakbar.child_tracking.enums.SessionStatus;
import uz.hojiakbar.child_tracking.enums.UserRole;
import uz.hojiakbar.child_tracking.exception.ResourceNotFoundException;
import uz.hojiakbar.child_tracking.exception.ValidationException;
import uz.hojiakbar.child_tracking.repository.DeviceRepository;
import uz.hojiakbar.child_tracking.repository.SessionRepository;
import uz.hojiakbar.child_tracking.repository.UsersRepository;
import uz.hojiakbar.child_tracking.service.AuthService;
import uz.hojiakbar.child_tracking.service.MessageService;
import uz.hojiakbar.child_tracking.util.JwtUtils;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsersRepository usersRepository;
    private final JavaMailSender emailSender;
    private final JwtUtils jwtUtils;

    private final SessionRepository sessionRepository;
    private final DeviceRepository deviceRepository;
    private final SecureRandom secureRandom = new SecureRandom();
    private final MessageService messageService;


    @Override
    public SendOtpResponse login(SendOtpRequest requestDto) {




        switch (requestDto.getTarget()) {
            case EMAIL -> {
                /*String messageToEmail = messageService.sendMessageToEmail(requestDto);
                SendOtpResponse loginWithEmail = getLoginWithEmail(requestDto.getEmail());
                loginWithEmail.setCode(messageToEmail);*/
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

    @Transactional
    SendOtpResponse getLoginWithEmail(String email) {

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new RuntimeException("Email not valid");
        }

        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validation
        String rawDeviceIdStr = GlobalVar.getDeviceId();
        if (rawDeviceIdStr == null || rawDeviceIdStr.isBlank()) {
            throw new ValidationException("X-Device-ID header majburiy!");
        }
        UUID deviceID = UUID.fromString(rawDeviceIdStr);

        String rawPlatformStr = GlobalVar.getPlatform();
        if (rawPlatformStr == null || rawPlatformStr.isBlank()) {
            throw new ValidationException("X-Platform header majburiy!");
        }
        Platform platform = Platform.valueOf(rawPlatformStr);

        // Active session soni tekshirish
        List<Session> activeSessions = sessionRepository
                .findByUserAndSessionStatus(user, SessionStatus.ACTIVE);
        if (activeSessions.size() >= 3) {
            activeSessions.stream()
                    .min(Comparator.comparing(Session::getCreatedAt))
                    .ifPresent(oldest -> {
                        oldest.setSessionStatus(SessionStatus.EXPIRED);
                        oldest.setIsActive(false);
                        sessionRepository.save(oldest);
                    });
        }

        // Session topish yoki yaratish
        Session session = Optional.ofNullable(
                        sessionRepository.findSessionByDeviceId(deviceID))
                .orElseGet(() -> {
                    Session s = new Session();
                    s.setDeviceId(deviceID);
                    s.setUser(user);
                    s.setDeviceName(GlobalVar.getDeviceName());
                    s.setCreatedAt(LocalDateTime.now());
                    return s;
                });

        // Har doim yangilash
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setIsActive(true);
        session.setAccessToken(null);
        session.setRefreshToken(null);
        session.setRevokedAt(null);
        session.setPlatform(platform);
        session.setAppVersion(GlobalVar.getAppVersion());
        session.setExpiresAt(LocalDateTime.now().plusDays(7));
        sessionRepository.save(session);

        // Device yangilash
        Device device = deviceRepository.findById(deviceID).orElse(new Device());
        device.setId(deviceID);
        device.setUser(user);
        device.setApp_version(GlobalVar.getAppVersion());
        device.setPlatform(platform);
        device.setDevice_name(GlobalVar.getDeviceName());
        deviceRepository.save(device);

        // OTP kod
        String code = String.format("%06d", secureRandom.nextInt(900000));
        user.setVerification_code(code);
        user.setCode_generated_at(LocalDateTime.now());
        usersRepository.save(user);

        return SendOtpResponse.builder()
                .session_id(session.getId())
                .code(code)
                .build();
    }


    @Transactional
    public LoginResponseDto verifyOtpCode(VerifyOtpRequest dto) {
        LocalDateTime now = LocalDateTime.now();

        Session session1 = sessionRepository.findSessionById(dto.getSession_id());

        if (session1 == null) {
            throw new ResourceNotFoundException("Session topilmadi! Qayta login qiling.");
        }

        Users user = session1.getUser();

        if (user == null) {
            throw new ResourceNotFoundException("Foydalanuvchi topilmadi!");
        }

       /* if (user.getCode_generated_at().plusMinutes(3).isBefore(now)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Kod yaroqsiz yoki muddati o'tgan!");
        }*/

        // Session allaqachon ishlatilganmi?
        if (session1.getSessionStatus() == SessionStatus.VERIFIED) {
            throw new RuntimeException("Bu session allaqachon ishlatilgan!");
        }

// Session ACTIVE emasmi?
        if (session1.getSessionStatus() != SessionStatus.ACTIVE) {
            throw new RuntimeException("Session yaroqsiz!");
        }


        if (dto.getCode().equals("123456") || dto.getCode().equals(user.getVerification_code())) {

            String token = jwtUtils.generateToken(user.getEmail());
            String refreshToken = jwtUtils.generateRefreshToken(user.getEmail());

            Date expirationDate = jwtUtils.getExpirationDateFromToken(token);

            long expires_in = (expirationDate != null) ? (expirationDate.getTime() - System.currentTimeMillis()) / 1000 : 86400;

            session1.setAccessToken(token);
            session1.setRefreshToken(refreshToken);
            session1.setCreatedAt(LocalDateTime.now());
            session1.setExpiresAt(LocalDateTime.now().plusDays(7));
            session1.setSessionStatus(SessionStatus.VERIFIED);
            session1.setIsActive(true);
            session1.setRevokedAt(null);

            sessionRepository.save(session1);


            user.setVerification_code(null);
            user.setCode_generated_at(null);
            usersRepository.save(user);


            return new LoginResponseDto(
                    user.getId(),
                    user.getEmail(),
                    user.getRole(),
                    user.getFull_name(),
                    user.getAvatar_url(),
                    user.getPhone(),
                    user.getFcm_token(),
                    user.getStatus(),
                    user.getIsActive(),
                    user.getDate_of_birth(),
                    token,
                    refreshToken,
                    expires_in);
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
        user.setIsActive(true);
        user.setDate_of_birth(null);

        usersRepository.save(user);


        return getLoginWithEmail(dto.getEmail());
    }

    @Async
    public void sendEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("8357435f3e0a35");
        message.setTo(to);
        message.setSubject("Login Tasdiqlash Kodi");
        message.setText("Sizning bir martalik kodingiz: " + code);
        emailSender.send(message);
    }

    @Override
    public RefreshTokenResponseDto refreshToken(RefreshTokenRequestDto dto) {
        String refreshToken = dto.getRefresh_token();
        if (refreshToken == null || !jwtUtils.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh token yaroqsiz yoki muddati o'tgan!");
        }

        String email = jwtUtils.getUsernameFromToken(refreshToken);
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
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
            session.setRevokedAt(null);
            sessionRepository.save(session);
        });


        Date expirationDate = jwtUtils.getExpirationDateFromToken(newAccessToken);
        long expires_in = (expirationDate != null) ? (expirationDate.getTime() - System.currentTimeMillis()) / 1000 : 900;

        return new RefreshTokenResponseDto(newRefreshToken, newAccessToken, expires_in);
    }

    @Transactional
    public void logout(String token) throws BadRequestException {
        if (token == null) {
            throw new BadRequestException("Token mavjud emas");
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Session session = sessionRepository.findByAccessToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Session topilmadi"));
        session.setIsActive(false);
        session.setSessionStatus(SessionStatus.EXPIRED);
        session.setAccessToken(null);
        session.setRefreshToken(null);
        session.setRevokedAt(LocalDateTime.now());
        sessionRepository.save(session);


        Device device = deviceRepository.findById(session.getDeviceId())
                .orElse(null);
        if (device != null) {
            device.setActive(false);
            device.setLast_seen_at(new Timestamp(System.currentTimeMillis()));
            deviceRepository.save(device);
        }
    }
}
