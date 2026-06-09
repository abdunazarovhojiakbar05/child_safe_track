package uz.hojiakbar.child_tracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.hojiakbar.child_tracking.entity.Notification;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    // 1. Bola IDsi bo'yicha barcha bildirishnomalarni olish (Yaratilgan vaqti bo'yicha eng yangilari birinchi)
    @Query("SELECT n FROM notifications n WHERE n.child_id = :childId ORDER BY n.created_at DESC")
    List<Notification> findAllByChildIdOrderByCreatedAtDesc(@Param("childId") UUID childId);

    // 2. Ota-ona (User) IDsi bo'yicha barcha bildirishnomalarni olish
    @Query("SELECT n FROM notifications n WHERE n.user_id = :userId ORDER BY n.created_at DESC")
    List<Notification> findAllByUserIdOrderByCreatedAtDesc(@Param("userId") UUID userId);

    // 3. Bola IDsi bo'yicha faqat o'qilmagan bildirishnomalarni olish
    @Query("SELECT n FROM notifications n WHERE n.child_id = :childId AND n.is_read = false ORDER BY n.created_at DESC")
    List<Notification> findUnreadByChildId(@Param("childId") UUID childId);

    // 4. Bolaga tegishli barcha bildirishnomalarni "O'qildi" deb belgilash (Siz qidirgan asosiy query)
    @Modifying
    @Transactional
    @Query("UPDATE notifications n SET n.is_read = true WHERE n.child_id = :childId AND n.is_read = false")
    void markAllAsReadByChildId(@Param("childId") UUID childId);

    // 5. Bitta aniq bildirishnomani "O'qildi" deb belgilash
    @Modifying
    @Transactional
    @Query("UPDATE notifications n SET n.is_read = true WHERE n.id = :notificationId")
    void markAsRead(@Param("notificationId") UUID notificationId);

    // 6. Bola IDsi bo'yicha o'qilmagan bildirishnomalar sonini hisoblash (Badge/Notification count uchun)
    @Query("SELECT COUNT(n) FROM notifications n WHERE n.child_id = :childId AND n.is_read = false")
    long countUnreadByChildId(@Param("childId") UUID childId);

    // 7. Ma'lum bir vaqtdan eski bo'lgan bildirishnomalarni o'chirish (Baza tozalash uchun)
    @Modifying
    @Transactional
    @Query("DELETE FROM notifications n WHERE n.created_at < :dateTime")
    void deleteOldNotifications(@Param("dateTime") java.time.LocalDateTime dateTime);
}