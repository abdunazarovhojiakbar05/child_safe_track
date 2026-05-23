package uz.hojiakbar.child_tracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.hojiakbar.child_tracking.entity.Geofences;

import java.util.UUID;

public interface GeofencesRepository extends JpaRepository<Geofences, UUID> {
}
