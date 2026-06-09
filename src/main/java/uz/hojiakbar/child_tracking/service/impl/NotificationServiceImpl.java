package uz.hojiakbar.child_tracking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.hojiakbar.child_tracking.entity.Notification;
import uz.hojiakbar.child_tracking.repository.NotificationRepository;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;
import uz.hojiakbar.child_tracking.service.NotificationService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public Notification getById(UUID id) {
        return new Notification();
    }

    @Override
    public void markAllAsRead(CustomUserDetails userDetails) {
    }



    @Override
    public List<Notification> getNotifications(CustomUserDetails userDetails) {
        if (userDetails.isParent()) {
            return notificationRepository
                    .findByUserIdOrderByCreatedAtDesc(userDetails.getUsers().getId());
        } else {
            return notificationRepository
                    .findByChildIdOrderByCreatedAtDesc(userDetails.getChild().getId());
        }
    }

}
