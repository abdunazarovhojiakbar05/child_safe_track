package uz.hojiakbar.child_tracking.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.hojiakbar.child_tracking.entity.Activities;
import uz.hojiakbar.child_tracking.enums.Activity_Type;

import java.util.List;
import java.util.UUID;

public interface ActivitiesRepository extends JpaRepository<Activities, UUID> {

    @Query("SELECT a FROM activities a WHERE a.child.id = :childId ORDER BY a.created_at DESC")
    List<Activities> findByChild_IdOrderByCreated_atDesc(@Param("childId") UUID childId);

    @Query("SELECT a FROM activities a WHERE a.child.id = :childId AND a.type = :type ORDER BY a.created_at DESC")
    List<Activities> findByChild_IdAndTypeOrderByCreated_atDesc(@Param("childId") UUID childId, @Param("type") Activity_Type type);
}