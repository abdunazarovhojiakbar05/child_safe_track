package uz.hojiakbar.child_tracking.repository;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.hojiakbar.child_tracking.entity.Locations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LocationsRepository extends JpaRepository<Locations, UUID> {

    // Oxirgi 1 ta location
    @Query("SELECT l FROM locations l WHERE l.child.id = :childId ORDER BY l.created_at DESC LIMIT 1")
    Optional<Locations> findLastByChildId(@Param("childId") UUID childId);

    // Route history
    @Query("SELECT l FROM locations l WHERE l.child.id = :childId ORDER BY l.recorded_at DESC")
    List<Locations> findAllByChildId(@Param("childId") UUID childId);

    // Bugungi locationlar
    @Query("SELECT l FROM locations l WHERE l.child.id = :childId AND l.recorded_at >= :from ORDER BY l.recorded_at DESC")
    List<Locations> findByChildIdAfter(@Param("childId") UUID childId, @Param("from") LocalDateTime from);
}