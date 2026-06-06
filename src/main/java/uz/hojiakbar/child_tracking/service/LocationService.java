package uz.hojiakbar.child_tracking.service;

import org.springframework.data.domain.Page;
import uz.hojiakbar.child_tracking.dto.request.LocationRequestDto;
import uz.hojiakbar.child_tracking.dto.response.LocationResponseDto;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;

import java.util.List;
import java.util.UUID;

public interface LocationService {

    void saveLocation(CustomUserDetails userDetails, LocationRequestDto dto);

    void checkGeofence(Child child, double lat, double lng);

    LocationResponseDto getLastLocation(UUID childId);

     List<LocationResponseDto> getRouteHistory(UUID childId);

    Page<LocationResponseDto> getRouteHistory(UUID childId, int page, int size);
}
