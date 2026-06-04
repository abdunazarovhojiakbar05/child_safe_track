package uz.hojiakbar.child_tracking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.hojiakbar.child_tracking.dto.auth.*;
import uz.hojiakbar.child_tracking.dto.refresh_token.RefreshTokenRequestDto;
import uz.hojiakbar.child_tracking.dto.refresh_token.RefreshTokenResponseDto;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Device;
import uz.hojiakbar.child_tracking.entity.Session;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.enums.UserRole;
import uz.hojiakbar.child_tracking.repository.ChildRepository;
import uz.hojiakbar.child_tracking.repository.DeviceRepository;
import uz.hojiakbar.child_tracking.repository.SessionRepository;
import uz.hojiakbar.child_tracking.repository.UsersRepository;
import uz.hojiakbar.child_tracking.service.AuthService;
import uz.hojiakbar.child_tracking.service.RefreshTokenService;
import uz.hojiakbar.child_tracking.util.JwtUtils;


import java.sql.Timestamp;
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
    public LoginResponseDto login(LoginRequestDto requestDto) {

        String email = requestDto.getEmail();

         if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new RuntimeException("Email formati noto'g'ri!");
        }

         Users user = usersRepository.findByEmail(requestDto.getEmail());

        if (user == null) {
            throw new RuntimeException("Foydalanuvchi topilmadi!");
        }

        if (requestDto.getCode() == null) {
            String code = String.valueOf((int) (Math.random() * 900000) + 100000);

            user.setVerification_code(code);
            user.setCode_generated_at(LocalDateTime.now());
            usersRepository.save(user);

            sendEmail(user.getEmail(), code);

            return new LoginResponseDto();
        }

        LocalDateTime now = LocalDateTime.now();

        if (user.getCode_generated_at().plusMinutes(2).isBefore(now)) {
            throw new RuntimeException("Kod yuborilmagan yoki eskirgan !");
        }

        if(requestDto.getCode().length() != 6){
            throw new RuntimeException("Kod formati noto'g'ri!");
        }

        if (requestDto.getCode().equals(user.getVerification_code())) {

            String token = jwtUtils.generateToken(user.getEmail());
            String refreshToken = jwtUtils.generateRefreshToken(user.getEmail());

            Date expirationDate = jwtUtils.getExpirationDateFromToken(token);

            long expires_in = (expirationDate != null) ? (expirationDate.getTime() - System.currentTimeMillis()) / 1000 : 86400;

            user.setVerification_code(null);
            usersRepository.save(user);


            Session session = Session.builder()
                    .user(user)
                    .accessToken(token)
                    .refreshToken(refreshToken)
                    .ipAddress("unknown") //TODO Haqiqiy IP ni olish uchun HttpServletRequest kerak
                    .userAgent("unknown")
                    .deviceId(UUID.randomUUID()) //TODO Bu yerda foydalanuvchi device_id sini yuborishi kerak
                    .createdAt(LocalDateTime.now())
                    .expiresAt(LocalDateTime.now().plusDays(7))
                    .revokedAt(null)
                    .build();
            sessionRepository.save(session);

            return new LoginResponseDto( new UserDto( user.getId(),  user.getFull_name() ), token, refreshToken,expires_in );
        } else {
            throw new RuntimeException("Kod xato!");
        }
    }



    @Override
    public RegistrationResponseDto registration( RegistrationRequestDto dto) {

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


    if (dto.getDevice() != null) {
        Device device = new Device();

        device.setDevice_model(dto.getDevice().getDevice_model());
        device.setOs_version(dto.getDevice().getOs_version());
        device.setApp_version(dto.getDevice().getApp_version());
        device.setPlatform(dto.getDevice().getPlatform());
        device.setDevice_token(dto.getDevice().getDevice_token());
        device.setDevice_name("asdf");

        devicesRepository.save(device);

    }
    RegistrationResponseDto responseDto = new RegistrationResponseDto();
    String accessToken = jwtUtils.generateToken(user.getEmail());
    String refreshToken = jwtUtils.generateRefreshToken(user.getEmail());

    responseDto.setAccess_token(accessToken);
    responseDto.setExpires_in(900);
    responseDto.setRefresh_token(refreshToken);
    responseDto.setUser(new UserDto( user.getId() , user.getFull_name()));

    Session session = Session.builder()
            .user(user)
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .ipAddress("unknown")
            .userAgent("unknown")
            .deviceId(UUID.randomUUID())
            .createdAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusDays(7))
            .build();
    sessionRepository.save(session);

    return   responseDto;
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


        sessionRepository. findByRefreshToken(refreshToken).ifPresent(session -> {
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
    public void logout( String token) {
        if (token != null) {
            refreshtokenService.deleteByAccessToken(token);
        }
    }
}
