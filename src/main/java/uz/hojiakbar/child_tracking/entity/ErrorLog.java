package uz.hojiakbar.child_tracking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity(name = "error_logs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorLog {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID user_id;

    @Column(columnDefinition = "TEXT")
    private String error_message;

    private String path;

    private int status;

    private String exception_type;

    private LocalDateTime created_at;


}
