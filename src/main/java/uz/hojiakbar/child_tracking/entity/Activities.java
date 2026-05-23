package uz.hojiakbar.child_tracking.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import uz.hojiakbar.child_tracking.enums.Activity_Type;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "activities")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Activities {
    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    Users child ;

    @Enumerated(EnumType.STRING)
    Activity_Type type;

    @Column(nullable = false, length = 100)
    String title;

    @Column(columnDefinition = "text")
    String description;

    BigDecimal location_lat;

    BigDecimal location_lon;

    int duration;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    Map<String, Object> metadata;

    @CreationTimestamp
    @Column(updatable = false)
    Timestamp created_at;

    Timestamp ended_at;


}
