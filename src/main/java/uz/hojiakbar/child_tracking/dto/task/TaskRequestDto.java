package uz.hojiakbar.child_tracking.dto.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;




@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDto {

    private UUID child_id;

    private String title;

    private String description;

    private LocalDateTime start_time;

    private LocalDateTime end_time;

    private UUID location_id;
}
