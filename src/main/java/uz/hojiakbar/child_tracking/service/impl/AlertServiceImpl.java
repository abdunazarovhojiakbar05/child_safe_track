package uz.hojiakbar.child_tracking.service.impl;

import jakarta.xml.bind.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.hojiakbar.child_tracking.entity.Alerts;
import uz.hojiakbar.child_tracking.exception.ResourceNotFoundException;
import uz.hojiakbar.child_tracking.repository.AlertsRepository;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;
import uz.hojiakbar.child_tracking.service.AlertService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {


    private final AlertsRepository alertsRepository;

    @Override
    public List<Alerts> getTimeline(UUID childId, LocalDate date) {

        LocalDate queryDate = date != null ? date : LocalDate.now();

        return alertsRepository.findTimelineByChildIdAndDate(childId, queryDate);

    }

    @Override
    public List<Alerts> getHistory(UUID childId) {
        return alertsRepository.findByChildId(childId);
    }

    @Override
    public List<Alerts> getUnread(CustomUserDetails userDetails) throws ValidationException {
        if(!userDetails.isParent()){
            throw new ValidationException("faqat ota va ona kora oladi ");
        }
        return alertsRepository.findUnreadByParentId(userDetails.getId());
    }

    @Override
    public void markAsRead(UUID alertId) {

        Alerts alert = alertsRepository.findById(alertId).orElseThrow(  ()->  new ResourceNotFoundException("Alert topilmadi"));
        alert.setRead(true);
        alertsRepository.save(alert);

    }

    @Override
    public void markAllAsRead(CustomUserDetails userDetails) throws ValidationException {

        if(!userDetails.isParent()){
            throw new ValidationException("faqat ota va ona kora oladi ");
        }

        List<Alerts> alerts = alertsRepository.findUnreadByParentId(userDetails.getId());
        alerts.forEach(alert -> alert.setRead(true));
        alertsRepository.saveAll(alerts);
    }
}
