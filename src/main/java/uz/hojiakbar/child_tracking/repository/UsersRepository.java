package uz.hojiakbar.child_tracking.repository;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.enums.UserRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<Users, UUID> {

    Optional<Users> findByEmail(String email);

    boolean existsByRole(UserRole userRole);

    boolean existsByPhone(String phone);

    boolean existsByEmail(@Email(message = "Email formati noto'g'ri") @NotBlank(message = "Email majburiy") String email);




}
