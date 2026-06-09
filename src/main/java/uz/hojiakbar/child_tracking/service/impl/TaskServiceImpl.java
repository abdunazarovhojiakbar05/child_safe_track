package uz.hojiakbar.child_tracking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.hojiakbar.child_tracking.dto.task.TaskRequestDto;
import uz.hojiakbar.child_tracking.dto.task.TaskResponseDto;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Locations;
import uz.hojiakbar.child_tracking.entity.Task;
import uz.hojiakbar.child_tracking.enums.Step;
import uz.hojiakbar.child_tracking.exception.ResourceNotFoundException;
import uz.hojiakbar.child_tracking.exception.ValidationException;
import uz.hojiakbar.child_tracking.repository.ChildRepository;
import uz.hojiakbar.child_tracking.repository.LocationsRepository;
import uz.hojiakbar.child_tracking.repository.TaskRepository;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;
import uz.hojiakbar.child_tracking.service.TaskService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ChildRepository childRepository;
    private final LocationsRepository locationsRepository;

    @Override
    public TaskResponseDto createTask(TaskRequestDto dto, CustomUserDetails userDetails) {
        if (!userDetails.isParent()) {
            throw new ValidationException("Faqat parent vazifa bera oladi!");
        }

        Child child = childRepository.findById(dto.getChild_id())
                .orElseThrow(() -> new ResourceNotFoundException("Bola topilmadi!"));

        Locations location = null;
        if (dto.getLocation_id() != null) {
            location = locationsRepository.findById(dto.getLocation_id())
                    .orElse(null);
        }

        Task task = Task.builder()
                .child(child)
                .parent(userDetails.getUsers())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .start_time(dto.getStart_time())
                .end_time(dto.getEnd_time())
                .step(Step.START)
                .is_done(false)
                .locations(location)
                .build();

        taskRepository.save(task);
        return toDto(task);
    }

    @Override
    public List<TaskResponseDto> getTasksByChild(UUID childId, CustomUserDetails userDetails) {
        if (!userDetails.isParent()) {
            throw new ValidationException("Faqat parent ko'ra oladi!");
        }
        return taskRepository.findTaskByChild_Id(childId)
                .stream().map(this::toDto).toList();
    }

    @Override
    public List<TaskResponseDto> getMyTasks(CustomUserDetails userDetails) {
        if (!userDetails.isChild()) {
            throw new ValidationException("Faqat bola o'z vazifalarini ko'ra oladi!");
        }
        return taskRepository.findTaskByChild_Id(userDetails.getChild().getId())
                .stream().map(this::toDto).toList();

    }

    @Override
    public TaskResponseDto markAsDone(UUID id, CustomUserDetails userDetails) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vazifa topilmadi!"));

        task.setIs_done(true);
        task.setStep(Step.FINISHED);
        taskRepository.save(task);
        return toDto(task);
    }

    @Override
    public TaskResponseDto updateTask(UUID id, TaskRequestDto dto, CustomUserDetails userDetails) {
        if (!userDetails.isParent()) {
            throw new ValidationException("Faqat parent tahrirlay oladi!");
        }

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vazifa topilmadi!"));

        if (dto.getTitle() != null) task.setTitle(dto.getTitle());
        if (dto.getDescription() != null) task.setDescription(dto.getDescription());
        if (dto.getStart_time() != null) task.setStart_time(dto.getStart_time());

        taskRepository.save(task);
        return toDto(task);
    }

    @Override
    public void deleteTask(UUID id, CustomUserDetails userDetails) {
        if (!userDetails.isParent()) {
            throw new ValidationException("Faqat parent o'chira oladi!");
        }
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vazifa topilmadi!"));
        taskRepository.delete(task);
    }

    private TaskResponseDto toDto(Task task) {

        return TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .is_done(task.getIs_done())
                .step(task.getStep())
                .created_at(task.getCreated_at())
                .child_id(task.getChild().getId())
                .child_name(task.getChild().getFull_name())
                .location_id(task.getLocations() != null ? task.getLocations().getId() : null)
                .build();
    }
}

