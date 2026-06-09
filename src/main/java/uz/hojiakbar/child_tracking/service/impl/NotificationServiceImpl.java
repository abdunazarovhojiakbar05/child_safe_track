package uz.hojiakbar.child_tracking.service.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.hojiakbar.child_tracking.dto.notification.NotificationRequest;
import uz.hojiakbar.child_tracking.entity.Notification;
import uz.hojiakbar.child_tracking.exception.ResourceNotFoundException;
import uz.hojiakbar.child_tracking.repository.ChildRepository;
import uz.hojiakbar.child_tracking.repository.NotificationRepository;
import uz.hojiakbar.child_tracking.repository.UsersRepository;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;
import uz.hojiakbar.child_tracking.service.NotificationService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final FirebaseMessaging firebaseMessaging;


    @Override
    public List<Notification> getNotifications(CustomUserDetails userDetails) {
        if (userDetails.isParent()) {
            return notificationRepository.findAllByUserIdOrderByCreatedAtDesc(userDetails.getUsers().getId());
        } else {
            return notificationRepository.findAllByChildIdOrderByCreatedAtDesc(userDetails.getChild().getId());
        }
    }

    @Override
    public Notification getById(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification topilmadi!"));

        notification.setIs_read(true);
        notificationRepository.save(notification);

        return notification;
    }

    @Override
    public void markAllAsRead(CustomUserDetails userDetails) {
        if (userDetails.isParent()) {
            notificationRepository.markAllAsReadByChildId(userDetails.getUsers().getId());
        } else {
            notificationRepository.markAllAsReadByChildId(userDetails.getChild().getId());
        }
    }

    @Override
    public void sendNotification(CustomUserDetails userDetails, NotificationRequest request) {

        String title = request.getTitle();
        String message = request.getMessage();
        String fcmToken = request.getFcm_token();

        UUID userId = request.getUser_id();
        UUID childId = request.getChild_id();

        Notification notification = Notification.builder()
                .title(title)
                .message(message)
                .user_id(userId)
                .child_id(childId)
                .is_read(false)
                .build();

        notificationRepository.save(notification);

        if (fcmToken != null && !fcmToken.isBlank()) {
            try {
                Message firebaseMessage = Message.builder()
                        .setToken(fcmToken)
                        .setNotification(com.google.firebase.messaging.Notification.builder()
                                .setTitle(title)
                                .setBody(message)
                                .build())
                        .build();
                firebaseMessaging.send(firebaseMessage);
                log.info("Firebase push notification muvaffaqiyatli yuborildi. Token: {}", fcmToken);
            } catch (Exception e) {
                log.warn("Firebase push yuborishda xatolik: {}", e.getMessage());
            }
        }
    }
}