package uz.hojiakbar.child_tracking.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorLogResponseDto {

     private UUID id;

    private UUID user_id;

     private String error_message;

    private String path;

    private int status;

    private String exception_type;

    private LocalDateTime created_at;
}
