package uz.hojiakbar.child_tracking.service;

import uz.hojiakbar.child_tracking.entity.Notification;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    Notification getById(UUID id);

    void markAllAsRead(CustomUserDetails userDetails);


    List<Notification> getNotifications(CustomUserDetails userDetails);
}
