package uz.hojiakbar.child_tracking.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    Users user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "child_id", nullable = true)
    Child child;

    @Column(name = "refresh_token", nullable = true, unique = true)
    String refreshToken;

    @Column(name = "access_token", nullable = true, unique = true)
    String accessToken;

    @Column(name = "device_id", nullable = false, unique = true)
    UUID deviceId;

    @Column(name = "ip_address", nullable = true)
    String ipAddress;

    @Column(name = "user_agent", nullable = true)
    String userAgent;

    @Column(name = "expires_at", nullable = false)
    LocalDateTime expiresAt;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;

     @Column(name = "revoked_at")
     LocalDateTime revokedAt;
}