package uz.hojiakbar.child_tracking.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import uz.hojiakbar.child_tracking.enums.Platform;

import java.sql.Timestamp;
 import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "device")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Device {
    @Id
    @GeneratedValue
    UUID id ;
    @Column(nullable = false)
    String device_name;

    @Column(nullable = false)
    String device_token;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Platform platform;

    @Column(nullable = false)
    String device_model;

    @Column(nullable = false)
    String os_version;

    @Column(nullable = false)
    String app_version;

    Timestamp last_seen_at;

    boolean isActive = false;
}
