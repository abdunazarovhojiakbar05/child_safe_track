package uz.hojiakbar.child_tracking.service;

import uz.hojiakbar.child_tracking.dto.notification.NotificationRequest;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Notification;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.repository.NotificationRepository;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    Notification getById(UUID id);

    void markAllAsRead(CustomUserDetails userDetails);

    void sendNotification(CustomUserDetails userDetails, NotificationRequest dto);


    List<Notification> getNotifications(CustomUserDetails userDetails);
}
