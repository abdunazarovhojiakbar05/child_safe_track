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

    private Boolean isDone;

    @Enumerated(EnumType.STRING)
    Step step;

    private LocalDateTime dueDate;

    private LocalDateTime createdAt;

    private UUID childId;

    private String childName;

    private UUID locationId;

}
