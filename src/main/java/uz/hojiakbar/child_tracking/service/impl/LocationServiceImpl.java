package uz.hojiakbar.child_tracking.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.hojiakbar.child_tracking.dto.request.LocationRequestDto;
import uz.hojiakbar.child_tracking.dto.response.LocationResponseDto;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Geofences;
import uz.hojiakbar.child_tracking.entity.Locations;
import uz.hojiakbar.child_tracking.exception.ResourceNotFoundException;
import uz.hojiakbar.child_tracking.repository.ChildRepository;
import uz.hojiakbar.child_tracking.repository.GeofencesRepository;
import uz.hojiakbar.child_tracking.repository.LocationsRepository;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;
import uz.hojiakbar.child_tracking.service.LocationService;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {


    private final LocationsRepository locationsRepository;
    private final ChildRepository childRepository;
    private final GeofencesRepository geofencesRepository;
    private final NotificationService1 notificationService;


    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {

        double R = 6371e3;
        double phi1 = Math.toRadians(lat1);
        double phi2 = Math.toRadians(lat2);
        double deltaPhi = Math.toRadians(lat2 - lat1);
        double deltaLambda = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2) +
                Math.cos(phi1) * Math.cos(phi2) *
                        Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    @Override
    public void saveLocation(CustomUserDetails userDetails, LocationRequestDto dto) throws BadRequestException {

        Child child = userDetails.getChild();

        if (child == null) {
            throw new BadRequestException("faqat bolangiz location yuborishis mumkin");
        }


        Child menageChild = childRepository.findById(child.getId()).orElseThrow(() -> new ResourceNotFoundException("child not found"));

        LocalDateTime recordedAt = dto.getTimestamp() != null
                ? LocalDateTime.ofInstant(
                Instant.ofEpochMilli(dto.getTimestamp()),
                ZoneId.systemDefault())
                : LocalDateTime.now();


        Locations locations = Locations.builder()
                .child(menageChild)
                .latitude(BigDecimal.valueOf(dto.getLatitude()))
                .longitude(BigDecimal.valueOf(dto.getLongitude()))
                .accuracy(dto.getAccuracy() != null ? dto.getAccuracy().floatValue() : null)
                .speed(dto.getSpeed() != null ? dto.getSpeed().floatValue() : null)
                .battery_level(dto.getBatteryLevel() != null ? dto.getBatteryLevel() : 0)
                .isCharging(Boolean.TRUE.equals(dto.getIsCharging()))
                .recorded_at(recordedAt)
                .build();

        locationsRepository.save(locations);

        checkGeofence(child, dto.getLatitude(), dto.getLongitude());

    }


    @Override
    public void checkGeofence(Child child, double lat, double lng) {
        List<Geofences> geofences = geofencesRepository.findActiveByChildId(child.getId());

        List<Geofences> toUpdate = new ArrayList<>();

        for (Geofences geofence : geofences) {
            double distance = calculateDistance(lat, lng,
                    geofence.getCenterLat().doubleValue(),
                    geofence.getCenterLon().doubleValue());

            boolean insideNow = distance <= geofence.getRadiusMetres().doubleValue();
            Boolean wasInside = geofence.getLastKnownInside();

            if (wasInside == null || wasInside != insideNow) {
                if (!insideNow && geofence.isNotifyOnExit()) {
                    notificationService.sendNotification(
                            geofence.getCreatedBy().getFcm_token(),
                            "⚠️ Xavfsiz hudud",
                            child.getFull_name() + " xavfsiz hududdan chiqdi: " + geofence.getName()
                    );
                }
                if (insideNow && geofence.isNotifyOnEnter()) {
                    notificationService.sendNotification(
                            geofence.getCreatedBy().getFcm_token(),
                            "✅ Xavfsiz hudud",
                            child.getFull_name() + " xavfsiz hududga kirdi: " + geofence.getName()
                    );
                }
                geofence.setLastKnownInside(insideNow);
                toUpdate.add(geofence);
            }
        }

        if (!toUpdate.isEmpty()) {
            geofencesRepository.saveAll(toUpdate);
        }
    }

    @Override
    public LocationResponseDto getLastLocation(UUID childId) {
        return locationsRepository
                .findLastByChildId(childId)
                .map(this::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Hali location yuborilmagan!"));
    }

    @Override
    public List<LocationResponseDto> getRouteHistory(UUID childId) {
        return locationsRepository
                .findAllByChildId(childId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public Page<LocationResponseDto> getRouteHistory(UUID childId, int page, int size) {
        LocalDateTime fifteenDaysAgo = LocalDateTime.now().minusDays(15);
        Pageable pageable = PageRequest.of(page, size);

        return locationsRepository
                .findByChildIdAndRecordedAtAfter(childId, fifteenDaysAgo, pageable)
                .map(this::toDto);
    }

    private LocationResponseDto toDto(Locations loc) {
        LocationResponseDto dto = new LocationResponseDto();
        dto.setLatitude(loc.getLatitude());
        dto.setLongitude(loc.getLongitude());
        dto.setSpeed(loc.getSpeed());
        dto.setAccuracy(loc.getAccuracy());
        dto.setBattery_level(loc.getBattery_level());
        dto.setIs_charging(loc.isCharging());
        dto.setRecorded_at(loc.getRecorded_at());
        dto.setCreated_at(loc.getCreated_at());
        dto.setAddress(null); ///TODO keyinroq reverse geocoding
        return dto;
    }
}
