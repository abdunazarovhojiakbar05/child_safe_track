package uz.hojiakbar.child_tracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.hojiakbar.child_tracking.entity.ErrorLog;

import java.util.UUID;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, UUID> {


}
