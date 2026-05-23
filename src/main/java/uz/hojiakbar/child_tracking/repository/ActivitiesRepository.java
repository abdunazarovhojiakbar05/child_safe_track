package uz.hojiakbar.child_tracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.hojiakbar.child_tracking.entity.Activities;

import java.util.UUID;

public interface ActivitiesRepository extends JpaRepository<Activities, UUID> {
}
