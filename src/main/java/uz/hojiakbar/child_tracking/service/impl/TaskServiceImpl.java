package uz.hojiakbar.child_tracking.service.impl;

import org.springframework.stereotype.Service;
import uz.hojiakbar.child_tracking.dto.task.TaskRequestDto;
import uz.hojiakbar.child_tracking.dto.task.TaskResponseDto;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;
import uz.hojiakbar.child_tracking.service.TaskService;

import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {
    @Override
    public TaskResponseDto createTask(TaskRequestDto dto, CustomUserDetails userDetails) {
        return null;
    }

    @Override
    public List<TaskResponseDto> getTasksByChild(UUID childId, CustomUserDetails userDetails) {
        return List.of();
    }

    @Override
    public List<TaskResponseDto> getMyTasks(CustomUserDetails userDetails) {
        return List.of();
    }

    @Override
    public void markAsDone(UUID id, CustomUserDetails userDetails) {

    }

    @Override
    public void deleteTask(UUID id, CustomUserDetails userDetails) {

    }

    @Override
    public TaskResponseDto updateTask(UUID id, TaskRequestDto dto, CustomUserDetails userDetails) {
        return null;
    }
}
