package uz.hojiakbar.child_tracking.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import uz.hojiakbar.child_tracking.enums.Alert_Severity;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "alerts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Alerts {

    @Id
    @GeneratedValue
    UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = false)
    Users parent_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id", nullable = false)
    Users child_id;

    @Enumerated(EnumType.STRING)
    Alert_Severity severity;

    @Column(nullable = false)
    String title;

    String message;

    boolean isRead;

    @CreationTimestamp
    Timestamp created_at;


}
