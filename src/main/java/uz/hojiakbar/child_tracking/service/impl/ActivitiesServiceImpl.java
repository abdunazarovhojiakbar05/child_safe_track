package uz.hojiakbar.child_tracking.service.impl;


import org.springframework.stereotype.Service;
import uz.hojiakbar.child_tracking.dto.response.ActivityResponseDto;
import uz.hojiakbar.child_tracking.entity.Activities;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.enums.Activity_Type;
import uz.hojiakbar.child_tracking.exception.ValidationException;
import uz.hojiakbar.child_tracking.repository.FamilyRelationsRepository;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;
import uz.hojiakbar.child_tracking.service.ActivitiesService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import lombok.RequiredArgsConstructor;
import uz.hojiakbar.child_tracking.repository.ActivitiesRepository;

@Service
@RequiredArgsConstructor
public class ActivitiesServiceImpl implements ActivitiesService {

    private final ActivitiesRepository activitiesRepository;
    private final FamilyRelationsRepository familyRelationsRepository;

    @Override
    public void save(Child child, Activity_Type type, String title, String description,
                     BigDecimal lat, BigDecimal lon, int duration, Map<String, Object> metadata) {
        Activities activity = Activities.builder()
                .child(child)
                .type(type)
                .title(title)
                .description(description)
                .location_lat(lat)
                .location_lon(lon)
                .duration(duration)
                .metadata(metadata)
                .build();
        activitiesRepository.save(activity);
    }

    @Override
    public List<ActivityResponseDto> getByChild(UUID childId, CustomUserDetails userDetails) {

        if (userDetails.isParent()) {
            boolean isMyChild = familyRelationsRepository
                    .findByParentEmail(userDetails.getUsers().getEmail())
                    .stream()
                    .anyMatch(r -> r.getChild().getId().equals(childId));
            if (!isMyChild) {
                throw new ValidationException("Bu bola sizning farzandingiz emas!");
            }
        }

        return activitiesRepository.findByChild_IdOrderByCreated_atDesc(childId)
                .stream().map(this::toDto).toList();
    }

    @Override
    public List<ActivityResponseDto> getByChildAndType(UUID childId, Activity_Type type, CustomUserDetails userDetails) {

        if (userDetails.isParent()) {
            boolean isMyChild = familyRelationsRepository
                    .findByParentEmail(userDetails.getUsers().getEmail())
                    .stream()
                    .anyMatch(r -> r.getChild().getId().equals(childId));
            if (!isMyChild) {
                throw new ValidationException("Bu bola sizning farzandingiz emas!");
            }
        }

        return activitiesRepository.findByChild_IdAndTypeOrderByCreated_atDesc(childId, type)
                .stream().map(this::toDto).toList();
    }


    private ActivityResponseDto toDto(Activities a) {
        return ActivityResponseDto.builder()
                .id(a.getId())
                .child_id(a.getChild().getId())
                .child_name(a.getChild().getFull_name())
                .type(a.getType())
                .title(a.getTitle())
                .description(a.getDescription())
                .location_lat(a.getLocation_lat())
                .location_lon(a.getLocation_lon())
                .duration(a.getDuration())
                .metadata(a.getMetadata())
                .created_at(a.getCreated_at())
                .ended_at(a.getEnded_at())
                .build();
    }
}