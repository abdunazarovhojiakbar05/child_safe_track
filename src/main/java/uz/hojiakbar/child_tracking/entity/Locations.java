package uz.hojiakbar.child_tracking.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "locations")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Locations {

    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    Users child;

    @Column(precision = 10, scale = 8, nullable = false)
    BigDecimal latitude;

    @Column(precision = 11, scale = 8, nullable = false)
    BigDecimal longitude;

    Float accuracy;

    Float speed;

    @Column(name = "battery_level")
    int battery_level;
    @Column(name = "is_charging")
    boolean isCharging;

    @Column(name = "recorded_at")
    LocalDateTime recorded_at;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime created_at;
}