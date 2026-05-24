package uz.hojiakbar.child_tracking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.hojiakbar.child_tracking.dto.request.LocationRequestDto;
import uz.hojiakbar.child_tracking.dto.response.LocationResponseDto;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Geofences;
import uz.hojiakbar.child_tracking.entity.Locations;
import uz.hojiakbar.child_tracking.repository.ChildRepository;
import uz.hojiakbar.child_tracking.repository.GeofencesRepository;
import uz.hojiakbar.child_tracking.repository.LocationsRepository;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;
import uz.hojiakbar.child_tracking.service.LocationService;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {


    private final LocationsRepository locationsRepository;
    private final ChildRepository childRepository;
    private final GeofencesRepository geofencesRepository;
    private final NotificationService notificationService;


    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {

        double R = 6371e3; /// yer radiusi ekan
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
    public void saveLocation(CustomUserDetails userDetails, LocationRequestDto dto) {

        Child child = userDetails.getChild();

        if (child == null) {
            throw new RuntimeException("faqat bolangiz location yuborishis mumkin");
        }


        Child menageChild = childRepository.findById(child.getId()).orElseThrow(() -> new RuntimeException("child not found"));

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


    private void checkGeofence(Child child, double lat, double lng) {

        List<Geofences> geofences = geofencesRepository.findActiveByChildId(child.getId());

        for (var geofence : geofences) {
            double distance = calculateDistance(lat, lng,
                    geofence.getCenterLat().doubleValue()
                    , geofence.getCenterLon().doubleValue()
            );


            boolean insideGeofence = distance <= geofence.getRadiusMetres().doubleValue();

            if (!insideGeofence && geofence.isNotifyOnExit()) {
                // ✅ Real notification
                notificationService.sendNotification(
                        geofence.getCreatedBy().getFcmToken(),
                        "⚠️ Xavfsiz hudud",
                        child.getFull_name() + " xavfsiz hududdan chiqdi: " + geofence.getName()
                );
            }
            if (insideGeofence && geofence.isNotifyOnEnter()) {
                // ✅ Real notification
                notificationService.sendNotification(
                        geofence.getCreatedBy().getFcmToken(),
                        "✅ Xavfsiz hudud",
                        child.getFull_name() + " xavfsiz hududga kirdi: " + geofence.getName()
                );
            }
        }

    }

    @Override
    public LocationResponseDto getLastLocation(UUID childId) {
        return locationsRepository
                .findLastByChildId(childId)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("Hali location yuborilmagan!"));
    }

    @Override
    public List<LocationResponseDto> getRouteHistory(UUID childId) {
        return locationsRepository
                .findAllByChildId(childId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    private LocationResponseDto toDto(Locations loc) {
        LocationResponseDto dto = new LocationResponseDto();
        dto.setLatitude(loc.getLatitude());
        dto.setLongitude(loc.getLongitude());
        dto.setSpeed(loc.getSpeed());
        dto.setAccuracy(loc.getAccuracy());
        dto.setBatteryLevel(loc.getBattery_level());
        dto.setIsCharging(loc.isCharging());
        dto.setRecordedAt(loc.getRecorded_at());
        dto.setCreatedAt(loc.getCreated_at());
        dto.setAddress(null); ///TODO keyinroq reverse geocoding
        return dto;
    }
}
