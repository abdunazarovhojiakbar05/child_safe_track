package uz.hojiakbar.child_tracking.service;


import uz.hojiakbar.child_tracking.dto.response.ActivityResponseDto;
import uz.hojiakbar.child_tracking.entity.Activities;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.enums.Activity_Type;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ActivitiesService
{
    void save(Child child, Activity_Type type, String title, String description,
              BigDecimal lat, BigDecimal lon, int duration, Map<String, Object> metadata);

    List<ActivityResponseDto> getByChild(UUID childId, CustomUserDetails userDetails);

    List<ActivityResponseDto> getByChildAndType(UUID childId, Activity_Type type, CustomUserDetails userDetails);
}