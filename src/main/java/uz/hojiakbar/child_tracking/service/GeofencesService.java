package uz.hojiakbar.child_tracking.service;

import jakarta.validation.Valid;
import uz.hojiakbar.child_tracking.dto.request.GeofenceRequestDto;
import uz.hojiakbar.child_tracking.dto.response.GeofenceResponseDto;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;

import java.util.List;
import java.util.UUID;

public interface GeofencesService {

     void deleteGeofence(UUID geofenceId);

    GeofenceResponseDto createGeofence(@Valid GeofenceRequestDto dto, CustomUserDetails userDetails);

    List<GeofenceResponseDto> getGeofencesByChildId(UUID childId);
}
