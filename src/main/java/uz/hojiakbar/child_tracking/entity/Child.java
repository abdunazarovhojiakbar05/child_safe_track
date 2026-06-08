package uz.hojiakbar.child_tracking.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import uz.hojiakbar.child_tracking.enums.Gender;
import uz.hojiakbar.child_tracking.enums.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity(name = "child")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Child {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", unique = true)
    UUID id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "child_parents",
            joinColumns = @JoinColumn(name = "child_id"),
            inverseJoinColumns = @JoinColumn(name = "parent_id"))
    List<Users> parents = new ArrayList<>();

    @Column(unique = true)
    String phone;

    int age;

    @Enumerated(EnumType.STRING)
    Status verified = Status.NOT_VERIFIED;

    String email;

    String full_name;

    String avatar_url;

    @Column(name = "date_of_birth")
    Date date_of_birth;

    @Enumerated(EnumType.STRING)
    Gender gender;

    Boolean isActive = true;

    @CreationTimestamp
    @Column(updatable = false)
    LocalDateTime created_at;

    @UpdateTimestamp
    LocalDateTime updated_at;
}
