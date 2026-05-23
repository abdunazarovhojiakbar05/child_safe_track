package uz.hojiakbar.child_tracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.hojiakbar.child_tracking.entity.Alerts;

import java.util.UUID;

public interface AlertsRepository extends JpaRepository <Alerts, UUID>{
}
