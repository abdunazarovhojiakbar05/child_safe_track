package uz.hojiakbar.child_tracking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.hojiakbar.child_tracking.enums.MessageStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
public class Message {
    @Id
    private UUID id;
    @Column(name = "email_or_phone",unique = true,nullable = false)
    private String phoneNumber;
    @Column(name = "code",nullable = false)
    private String code;
    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageStatus status;
    @Column(name = "created_at")
    private LocalDateTime createdAt=LocalDateTime.now();
    @Column(name = "checked_at")
    private LocalDateTime checkedAt;

    public Message(UUID uuid, String email, String code, MessageStatus messageStatus) {
        this.id = uuid;
        this.phoneNumber = email;
        this.code = code;
        this.status = messageStatus;
        this.createdAt = LocalDateTime.now();
        this.checkedAt = LocalDateTime.now();
    }
}
