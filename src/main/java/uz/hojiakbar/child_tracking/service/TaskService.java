package uz.hojiakbar.child_tracking.service;

import uz.hojiakbar.child_tracking.dto.task.TaskRequestDto;
import uz.hojiakbar.child_tracking.dto.task.TaskResponseDto;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;

import java.util.List;
import java.util.UUID;

public interface TaskService {
    TaskResponseDto createTask(TaskRequestDto dto, CustomUserDetails userDetails);

    List<TaskResponseDto> getTasksByChild(UUID childId, CustomUserDetails userDetails);

    List<TaskResponseDto> getMyTasks(CustomUserDetails userDetails);

    void markAsDone(UUID id, CustomUserDetails userDetails);

    void deleteTask(UUID id, CustomUserDetails userDetails);

    TaskResponseDto updateTask(UUID id, TaskRequestDto dto, CustomUserDetails userDetails);
}
