package uz.hojiakbar.child_tracking.service;

import uz.hojiakbar.child_tracking.dto.request.LocationRequestDto;
import uz.hojiakbar.child_tracking.dto.response.LocationResponseDto;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;

import java.util.List;
import java.util.UUID;

public interface LocationService {

    void saveLocation(CustomUserDetails userDetails, LocationRequestDto dto);

     LocationResponseDto getLastLocation(UUID childId);

     List<LocationResponseDto> getRouteHistory(UUID childId);
}
