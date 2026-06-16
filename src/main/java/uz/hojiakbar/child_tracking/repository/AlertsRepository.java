package uz.hojiakbar.child_tracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.hojiakbar.child_tracking.entity.Alerts;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.enums.Alert_Type;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AlertsRepository extends JpaRepository<Alerts, UUID> {

    @Query(value = "SELECT * FROM alerts WHERE parent_id = :parentId ORDER BY created_at DESC",
            nativeQuery = true)
    List<Alerts> findByParentId(@Param("parentId") UUID parentId);

    @Query(value = "SELECT * FROM alerts WHERE child_id = :childId ORDER BY created_at DESC",
            nativeQuery = true)
    List<Alerts> findByChildId(@Param("childId") UUID childId);

    @Query(value = "SELECT * FROM alerts WHERE parent_id = :parentId AND type = :type ORDER BY created_at DESC",
            nativeQuery = true)
    List<Alerts> findByParentIdAndType(@Param("parentId") UUID parentId, @Param("type") Alert_Type type);


    @Query(value = "SELECT COUNT(*) FROM alerts WHERE parent_id = :parentId AND DATE(created_at) = CURRENT_DATE",
            nativeQuery = true)
    int countTodayAlerts(@Param("parentId") UUID parentId);

    @Query(value = "SELECT COUNT(*) FROM alerts WHERE parent_id = :parentId AND is_read = false",
            nativeQuery = true)
    int countUnreadAlerts(@Param("parentId") UUID parentId);

    @Query(value = "SELECT * FROM alerts WHERE parent_id = :parentId AND is_read = false ORDER BY created_at DESC",
            nativeQuery = true)
    List<Alerts> findUnreadByParentId(@Param("parentId") UUID parentId);

    @Query(value = "SELECT * FROM alerts WHERE child_id = :childId AND DATE(created_at) = :date ORDER BY created_at ASC",
            nativeQuery = true)
    List<Alerts> findTimelineByChildIdAndDate(
            @Param("childId") UUID childId,
            @Param("date") LocalDate date
    );

    @Query(value = "SELECT COUNT(*) FROM alerts WHERE parent_id = :parentId AND is_read = false",
            nativeQuery = true)
    int countUnreadByUsers(@Param("parentId") UUID parentId);
}