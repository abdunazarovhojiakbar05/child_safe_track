package uz.hojiakbar.child_tracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.w3c.dom.stylesheets.LinkStyle;
import uz.hojiakbar.child_tracking.entity.Geofences;

import java.util.List;
import java.util.UUID;

public interface GeofencesRepository extends JpaRepository<Geofences, UUID> {

    @Query("SELECT g FROM geofences g WHERE g.child.id = :childId AND g.isActive = true")
    List<Geofences> findActiveByChildId(@Param("childId") UUID childId);

    @Query("SELECT g FROM geofences g WHERE g.child.id = :childId")
    List<Geofences> findAllByChildId(@Param("childId") UUID childId);
}
