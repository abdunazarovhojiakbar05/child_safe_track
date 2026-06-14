package uz.hojiakbar.child_tracking.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id ;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users user;

    @ManyToOne
    @JoinColumn(name = "child_id")
    @JsonIgnore
    private Child child;


    @Column(nullable = true)
    String device_name;

    @Column(nullable = true)
    String device_token;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Platform platform;

    @Column(nullable = true)
    String device_model;

    @Column(nullable = true)
    String os_version;

    @Column(nullable = true)
    String app_version;


    Timestamp last_seen_at;

    boolean isActive = false;
}
