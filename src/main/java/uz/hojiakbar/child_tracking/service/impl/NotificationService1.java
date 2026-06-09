package uz.hojiakbar.child_tracking.service.impl;


import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public   class NotificationService1 {

    public void sendNotification(String fcmToken, String title, String body) {

        if(fcmToken == null || fcmToken.isEmpty()) {
            System.out.println("FCM token is null or empty");
            return;
        }


        Message message = Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();


        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("✅ Notification yuborildi: " + response);
        } catch (FirebaseMessagingException e) {
            System.out.println("❌ Notification xato: " + e.getMessage());
        }
    }


 }
