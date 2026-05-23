package uz.hojiakbar.child_tracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.hojiakbar.child_tracking.entity.Locations;

import java.util.UUID;

public interface LocationsRepository extends JpaRepository<Locations, UUID> {
}
