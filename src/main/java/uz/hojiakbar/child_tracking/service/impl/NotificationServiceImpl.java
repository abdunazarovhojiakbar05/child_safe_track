package uz.hojiakbar.child_tracking.service.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.hojiakbar.child_tracking.dto.notification.NotificationRequest;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Notification;
import uz.hojiakbar.child_tracking.entity.Users;
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

    private final UsersRepository usersRepository;
    private final ChildRepository childRepository;

    @Override
    public List<Notification> getNotifications(CustomUserDetails userDetails) {
        if (userDetails.isParent()) {
            return notificationRepository
                    .findNotificationByUser_Id(userDetails.getUsers().getId());
        } else {
            return notificationRepository
                    .findNotificationByChild_Id(userDetails.getChild().getId());
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
            notificationRepository.markAllAsReadByUserId(userDetails.getUsers().getId());
        } else {
            notificationRepository.markAllAsReadByChildId(userDetails.getChild().getId());
        }
    }

    @Override
    public void sendNotification(CustomUserDetails userDetails, NotificationRequest request) {



        String title = request.getTitle();
        String message = request.getMessage();
        String fcmToken = request.getFcmToken();

        Users user = request.getUser_id() != null
                ? usersRepository.findById(request.getUser_id()).orElse(null)
                : null;

        Child child = request.getChild_id() != null
                ? childRepository.findById(request.getChild_id()).orElse(null)
                : null;


                Notification notification = Notification.builder()
                .title(title)
                .message(message)
                .is_read(false)
                .build();

        notification.setUser(user);
        notification.setChild(child);
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
            } catch (Exception e) {
                log.warn("Firebase push yuborishda xatolik: {}", e.getMessage());
            }
        }
    }
}