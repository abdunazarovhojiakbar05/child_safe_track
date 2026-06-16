package uz.hojiakbar.child_tracking.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import uz.hojiakbar.child_tracking.enums.Geofences_Type;

import java.math.BigDecimal;
import java.sql.Timestamp;
 import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "geofences")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Geofences {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id ;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    Child child;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    Users createdBy;

    @Column(nullable = false)
    String name;

    Boolean lastKnownInside;

    BigDecimal centerLat;

    BigDecimal centerLon;

    BigDecimal radiusMetres;

    @Enumerated(EnumType.STRING)
    Geofences_Type type;

    boolean isActive = true;
    boolean notifyOnEnter = true;
    boolean notifyOnExit = true;


    @CreationTimestamp
    Timestamp createdAt;

}
