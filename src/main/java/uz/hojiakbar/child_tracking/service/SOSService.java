package uz.hojiakbar.child_tracking.service;

import jakarta.xml.bind.ValidationException;
import uz.hojiakbar.child_tracking.entity.Alerts;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface SOSService {
    void triggerSos(CustomUserDetails userDetails, BigDecimal latitude, BigDecimal longitude) throws ValidationException;

    List<Alerts> getSosHistory(CustomUserDetails userDetails);

    void resolveAlert(UUID alertId);
}
