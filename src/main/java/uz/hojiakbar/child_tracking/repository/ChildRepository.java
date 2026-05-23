package uz.hojiakbar.child_tracking.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.hojiakbar.child_tracking.entity.Child;

import java.util.UUID;

public interface ChildRepository extends JpaRepository<Child, UUID> {

    Child findByPhone(String phone);
}
