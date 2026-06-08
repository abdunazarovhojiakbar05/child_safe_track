package uz.hojiakbar.child_tracking.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.hojiakbar.child_tracking.enums.Platform;

import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "sessions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
     UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = true)
    Child child;

    @Column(name = "refresh_token", nullable = true, unique = true)
    String refreshToken;

    @Column(name = "access_token", nullable = true, unique = true)
    String accessToken;

    @Column(name = "device_id", nullable = false)
    UUID deviceId;

    @Column(name = "device_name", nullable = true)
    String deviceName;

    @Column(name = "ip_address", nullable = true)
    String ipAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = true)
    Platform platform;

    @Column(name = "app_version", nullable = true)
    String appVersion;

    @Column(name = "expires_at", nullable = false)
    LocalDateTime expiresAt;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

     @Column(name = "revoked_at")
     LocalDateTime revokedAt;

    @Column(name = "is_active")
    Boolean isActive = true;
}