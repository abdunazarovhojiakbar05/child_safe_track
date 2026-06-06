package uz.hojiakbar.child_tracking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.hojiakbar.child_tracking.dto.request.GeofenceRequestDto;
import uz.hojiakbar.child_tracking.dto.response.GeofenceResponseDto;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Geofences;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.enums.UserRole;
import uz.hojiakbar.child_tracking.repository.ChildRepository;
import uz.hojiakbar.child_tracking.repository.GeofencesRepository;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;
import uz.hojiakbar.child_tracking.service.GeofencesService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GeofencesServiceImpl implements GeofencesService {

    private final ChildRepository childRepository;
    private final GeofencesRepository geofencesRepository;

    @Override
    public void deleteGeofence(UUID geofenceId) {

        Geofences geofence = geofencesRepository.findById(geofenceId).orElseThrow(() -> new RuntimeException("Geofence not found"));
        geofence.setActive(false);
        geofencesRepository.save(geofence);

    }

    @Override
    public GeofenceResponseDto createGeofence(GeofenceRequestDto dto, CustomUserDetails userDetails) {

        Child child = childRepository.findById(dto.getChildId()).orElseThrow(() -> new RuntimeException("child not found"));

        Users parent = userDetails.getUsers();

        if(!parent.getRole( ).equals(UserRole.PARENT)){
            throw new RuntimeException("Bu holatda geofencing qo'shish mumkin emas | faqat ota onalar qo'sha oladi ");
        }

        // 3. Saqlash
        Geofences geofence = Geofences.builder()
                .child(child)
                .name(dto.getName())
                .centerLat(BigDecimal.valueOf(dto.getCenterLat()))
                .centerLon(BigDecimal.valueOf(dto.getCenterLon()))
                .radiusMetres(BigDecimal.valueOf(dto.getRadiusMetres()))
                .type(dto.getType())
                .isActive(true)
                .notifyOnEnter(Boolean.TRUE.equals(dto.getNotifyOnEnter()))
                .notifyOnExit(Boolean.TRUE.equals(dto.getNotifyOnExit()))
                .createdBy(parent)
                .build();

        Geofences saved = geofencesRepository.save(geofence);
        return toDto(saved);
    }

    private GeofenceResponseDto toDto(Geofences g) {
        return GeofenceResponseDto.builder()
                .id(g.getId())
                .name(g.getName())
                .centerLat(g.getCenterLat().doubleValue())
                .centerLon(g.getCenterLon().doubleValue())
                .radiusMetres(g.getRadiusMetres().doubleValue())
                .type(g.getType())
                .isActive(g.isActive())
                .notifyOnEnter(g.isNotifyOnEnter())
                .notifyOnExit(g.isNotifyOnExit())
                .build();
    }


    @Override
    public List<GeofenceResponseDto> getGeofencesByChildId(UUID childId) {
        return  geofencesRepository.findAllByChildId(childId).stream().map(this::toDto).toList();
    }
}
