package uz.hojiakbar.child_tracking.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.hojiakbar.child_tracking.dto.task.TaskRequestDto;
import uz.hojiakbar.child_tracking.dto.task.TaskResponseDto;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;
import uz.hojiakbar.child_tracking.service.TaskService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @Operation(summary = "Vazifa yaratish", description = "Bolaning vazifalari yaratish")
    public ResponseEntity<TaskResponseDto> create(
            @RequestBody TaskRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(taskService.createTask(dto, userDetails));
    }

    @GetMapping("/{childId}")
    @Operation(summary = "Bolaning vazifalari", description = "Bolaning vazifalari ro'yxatini olish")
    public ResponseEntity<List<TaskResponseDto>> getByChild(
            @PathVariable UUID childId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(taskService.getTasksByChild(childId, userDetails));
    }

    @GetMapping("/my")
    @Operation(summary = "Bola o'zining vazifalari", description = "Bola o'zining vazifalari ro'yxatini olish")
    public ResponseEntity<List<TaskResponseDto>> getMy(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(taskService.getMyTasks(userDetails));
    }

    @PutMapping("/{id}/done")
    @Operation(summary = "Vazifa bajarildi", description = "Bola o'z vazifasini bajarish")
    public ResponseEntity<String> markDone(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        taskService.markAsDone(id, userDetails);
        return ResponseEntity.ok("Vazifa bajarildi!");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Vazifa o'chirish", description = "Bolaning vazifasini o'chirish")
    public ResponseEntity<String> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        taskService.deleteTask(id, userDetails);
        return ResponseEntity.ok("Vazifa o'chirildi!");
    }

    @PutMapping("/{id}")
    @Operation(summary = "Vazifa tahrirlash", description = "Bolaning vazifasini tahrirlash")
    public ResponseEntity<TaskResponseDto> update(
            @PathVariable UUID id,
            @RequestBody TaskRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(taskService.updateTask(id, dto, userDetails));
    }
}
