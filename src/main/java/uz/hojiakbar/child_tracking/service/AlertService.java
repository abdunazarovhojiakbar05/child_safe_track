package uz.hojiakbar.child_tracking.service;

import jakarta.xml.bind.ValidationException;
import uz.hojiakbar.child_tracking.entity.Alerts;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AlertService {


    List<Alerts> getTimeline(UUID childId, LocalDate date);

    List<Alerts> getHistory(UUID childId);

    List<Alerts> getUnread(CustomUserDetails userDetails) throws ValidationException;

    void markAsRead(UUID alertId);

    void markAllAsRead(CustomUserDetails userDetails) throws ValidationException;

}
