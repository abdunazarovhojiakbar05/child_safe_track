package uz.hojiakbar.child_tracking.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import uz.hojiakbar.child_tracking.enums.Gender;
import uz.hojiakbar.child_tracking.enums.UserRole;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "fcm_token")
    String fcmToken;

    @Column(unique = true)
    String verification_code;

    @ManyToMany(fetch = FetchType.EAGER)
    List<Child> children = new ArrayList<>();

    LocalDateTime code_generated_at;

    @Enumerated(EnumType.STRING)
    UserRole role;

    @Column(unique = true, nullable = false)
    String email;

    @Column(unique = true)
    String phone;

    @Column(unique = true, nullable = false)
    String password_hash;

    @Column(nullable = false)
    String full_name;

    String avatar_url;


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
