package uz.hojiakbar.child_tracking.dto.task;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import uz.hojiakbar.child_tracking.enums.Step;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskResponseDto {

    private UUID id;

    private String title;

    private String description;

    private Boolean is_done;

    @Enumerated(EnumType.STRING)
    Step step;

    private LocalDateTime start_time;

    private LocalDateTime created_at;

    private UUID child_id;

    private String child_name;

    private UUID location_id;

}
