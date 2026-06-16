package uz.hojiakbar.child_tracking.service.impl;

import jakarta.xml.bind.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import uz.hojiakbar.child_tracking.dto.notification.NotificationRequest;
import uz.hojiakbar.child_tracking.entity.Alerts;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Family_Relations;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.enums.Activity_Type;
import uz.hojiakbar.child_tracking.enums.Alert_Severity;
import uz.hojiakbar.child_tracking.enums.Alert_Type;
import uz.hojiakbar.child_tracking.exception.ResourceNotFoundException;
import uz.hojiakbar.child_tracking.repository.AlertsRepository;
 import uz.hojiakbar.child_tracking.repository.FamilyRelationsRepository;
 import uz.hojiakbar.child_tracking.security.CustomUserDetails;
import uz.hojiakbar.child_tracking.service.ActivitiesService;
import uz.hojiakbar.child_tracking.service.NotificationService;
import uz.hojiakbar.child_tracking.service.SOSService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class SOSServiceImpl implements SOSService {



    private final FamilyRelationsRepository familyRelationsRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final AlertsRepository alertsRepository;
    private final NotificationService notificationService;
    private final ActivitiesService activitiesService;


    @Override
    public void triggerSos(CustomUserDetails userDetails, BigDecimal latitude, BigDecimal longitude) throws ValidationException {

        if (!userDetails.isChild()) {
            throw new ValidationException("faqat bola SOS yuborishi mumkin");
        }
        Child child = userDetails.getChild();


        List<Family_Relations> relation = familyRelationsRepository.findAllByChildEmail(child.getEmail());
        if (relation.isEmpty()) {
            throw new ResourceNotFoundException("Family relation topilmadi!");
        }

        for (Family_Relations relations : relation) {
            Users parent = relations.getParent();
            if (parent == null) continue;

            Alerts alert = Alerts.builder()
                    .type(Alert_Type.SOS)
                    .severity(Alert_Severity.CRITICAL)
                    .title("🆘 SOS signal")
                    .message(child.getFull_name() + " yordam sorayapti!")
                    .isRead(false)
                    .child_id(child)
                    .parent_id(parent)
                    .build();
            alertsRepository.save(alert);


            activitiesService.save(
                    child,
                    Activity_Type.SOS_TRIGGERED,
                    "SOS signal yuborildi",
                    child.getFull_name() + " yordam so'radi",
                    latitude != null ? latitude : null,
                    longitude != null ? longitude : null,
                    0,
                    null
            );

            messagingTemplate.convertAndSend("/topic/alerts/" + parent.getId(), alert);

            NotificationRequest notificationRequest = new NotificationRequest();
            notificationRequest.setTitle("🆘 SOS Signal!");
            notificationRequest.setMessage(child.getFull_name() + " yordam so'rayapti!");
            notificationRequest.setFcm_token(parent.getFcm_token());
            notificationRequest.setUser_id(parent.getId());
            notificationRequest.setChild_id(child.getId());

            notificationService.sendNotification(userDetails, notificationRequest);
        }
    }

    @Override
    public List<Alerts> getSosHistory(CustomUserDetails userDetails) {
        return alertsRepository.findByParentId(userDetails.getUsers().getId());
    }

    @Override
    public void resolveAlert(UUID alertId) {
        Alerts alert = alertsRepository.findById(alertId).orElseThrow(  ()->  new ResourceNotFoundException("Alert topilmadi"));
        alert.setRead(true);
        alertsRepository.save(alert);
    }
}
