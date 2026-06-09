package uz.hojiakbar.child_tracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uz.hojiakbar.child_tracking.entity.Notification;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {



    List<Notification> findNotificationByUser_Id(UUID userId);

    List<Notification> findNotificationByChild_Id (UUID id);

    @Modifying
    @Transactional
    @Query("UPDATE notifications  n SET n.is_read = true WHERE n.user.id = :userId")
    void markAllAsReadByUserId(@Param("userId") UUID userId);

    @Modifying
    @Transactional
    @Query("UPDATE notifications n SET n.is_read = true WHERE n.child.id = :childId")
    void markAllAsReadByChildId(@Param("childId") UUID childId);
}
