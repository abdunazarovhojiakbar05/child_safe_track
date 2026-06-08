package uz.hojiakbar.child_tracking.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import uz.hojiakbar.child_tracking.enums.Relation_Type;
import uz.hojiakbar.child_tracking.enums.Status;

import java.sql.Timestamp;
 import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "family_relations")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Family_Relations {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
     UUID id ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = true)
    Users parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    Child child;

    @Enumerated(EnumType.STRING)
    Relation_Type type;


    @Enumerated(EnumType.STRING)
    Status status;

    @CreationTimestamp
    Timestamp created_at;


}
