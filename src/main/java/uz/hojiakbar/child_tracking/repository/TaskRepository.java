package uz.hojiakbar.child_tracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.hojiakbar.child_tracking.entity.Task;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    Task findByTitle(String title);
}
