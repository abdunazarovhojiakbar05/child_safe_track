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
    @GeneratedValue
     UUID id ;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    Users child_id;

    @Column(nullable = false)
    String name;

    BigDecimal center_lat;

    BigDecimal center_lon;

    BigDecimal radius_metres;

    @Enumerated(EnumType.STRING)
    Geofences_Type type;

    boolean isActive = true;
    boolean notify_on_enter = true;
    boolean notify_on_exit = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    Users created_by; //   By Parent

    @CreationTimestamp
    Timestamp created_at;

}
