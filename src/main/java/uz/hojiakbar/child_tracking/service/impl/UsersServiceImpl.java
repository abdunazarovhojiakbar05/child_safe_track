package uz.hojiakbar.child_tracking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.hojiakbar.child_tracking.dto.parentDto.*;
import uz.hojiakbar.child_tracking.dto.response.ActivitySummaryResponseDto;
import uz.hojiakbar.child_tracking.dto.response.AlertResponseDto;
import uz.hojiakbar.child_tracking.dto.response.GeofenceResponseDto;
import uz.hojiakbar.child_tracking.dto.response.LocationResponseDto;
import uz.hojiakbar.child_tracking.entity.*;
import uz.hojiakbar.child_tracking.exception.ResourceNotFoundException;
import uz.hojiakbar.child_tracking.repository.*;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;
import uz.hojiakbar.child_tracking.service.UsersService;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {


    private final UsersRepository usersRepository;
    private final ChildRepository childRepository;
    private final FamilyRelationsRepository familyRepository;
    private final LocationsRepository locationsRepository;
    private final GeofencesRepository geofencesRepository;
    private final AlertsRepository alertsRepository;



    @Override
    @Transactional(readOnly = true)
    public ParentDashboardResponseDto getParentDashboard(String emailUser) {


        List<Family_Relations > relation = familyRepository.findByParentEmail(emailUser);


        List<ChildDashboardDto> children = new ArrayList<>(relation.stream()
                .filter(r -> r.getChild() != null)
                .map(r -> {
                    Child child = r.getChild();


                    LocationResponseDto location = locationsRepository
                            .findLastByChildId(child.getId())
                            .map(this::toLocationDto)
                            .orElse(null);

                    return new ChildDashboardDto(
                            child.getId(),
                            child.getFull_name(),
                            child.getAvatar_url(),
                            child.getIsActive(),
                            false,
                            location,
                            null,
                            null
                    );
                }).toList());

        Users parent = usersRepository.findByEmail(emailUser);
        if (parent == null) {
            throw new ResourceNotFoundException("Foydalanuvchi topilmadi: " + emailUser);
        }

        List<GeofenceResponseDto> geofences = new ArrayList<>();
        for (Family_Relations r : relation) {
            if (r.getChild() != null) {
                geofencesRepository.findActiveByChildId(r.getChild().getId())
                        .stream()
                        .map(this::toGeofenceDto)
                        .forEach(geofences::add);
            }
        }

        List<AlertResponseDto> recentAlerts = alertsRepository
                .findByParentId(parent.getId())
                .stream()
                .limit(5)
                .map(this::toAlertDto)
                .toList();

        SummaryResponseDto summary = new SummaryResponseDto();
        summary.setActiveChildren((int) relation.stream()
                .filter(r -> r.getChild() != null && Boolean.TRUE.equals(r.getChild().getIsActive()))
                .count());
        summary.setTotalAlertsToday(alertsRepository.countTodayAlerts(parent.getId()));
        summary.setUnreadAlerts(alertsRepository.countUnreadAlerts(parent.getId()));

        List<ActivitySummaryResponseDto> dailyActivitySummary = new ArrayList<>();

        return new ParentDashboardResponseDto(summary, children, geofences, dailyActivitySummary, recentAlerts);
    }


    private GeofenceResponseDto toGeofenceDto(Geofences g) {
        GeofenceResponseDto dto = new GeofenceResponseDto();
        dto.setId(g.getId());
        dto.setName(g.getName());
        dto.setType(g.getType());
        dto.setCenter_lat(g.getCenterLat() != null ? g.getCenterLat().doubleValue() : null);
        dto.setCenter_lon(g.getCenterLon() != null ? g.getCenterLon().doubleValue() : null);
        dto.setRadius_metres(g.getRadiusMetres() != null ? g.getRadiusMetres().doubleValue() : null);
        dto.set_active(g.isActive());
        dto.setNotify_on_enter(g.isNotifyOnEnter());
        dto.setNotify_on_exit(g.isNotifyOnExit());
        return dto;
    }

    private AlertResponseDto toAlertDto(Alerts a) {
        AlertResponseDto dto = new AlertResponseDto();
        dto.setId(a.getId());
        dto.setTitle(a.getTitle());
        dto.setType(a.getType());
        dto.setSeverity(a.getSeverity());
        dto.setCreated_at(a.getCreated_at() != null ? a.getCreated_at().toLocalDateTime() : null);
        return dto;
    }

    private LocationResponseDto toLocationDto(Locations loc) {
        LocationResponseDto dto = new LocationResponseDto();
        dto.setLatitude(loc.getLatitude());
        dto.setLongitude(loc.getLongitude());
        dto.setSpeed(loc.getSpeed());
        dto.setAccuracy(loc.getAccuracy());
        dto.setBattery_level(loc.getBattery_level());
        dto.setIs_charging(loc.isCharging());
        dto.setRecorded_at(loc.getRecorded_at());
        dto.setCreated_at(loc.getCreated_at());
        dto.setAddress(null);
        return dto;
    }



    @Override
    @Transactional(readOnly = true)
    public List<ChildListResponseDto> getChildrenByParentEmail(String email) {


        List<Family_Relations> list = familyRepository.findByParentEmail(email);

        if(list.isEmpty()){
            return List.of();
        }

        return list.stream()
                .filter(relation -> relation.getChild() != null)
                .map(relation -> {
                    Child child = relation.getChild();
                    return ChildListResponseDto.builder()
                            .id(child.getId())
                            .full_name(child.getFull_name())
                            .phone(child.getPhone())
                            .avatar_url(child.getAvatar_url())
                            .date_of_birth(child.getDate_of_birth())
                            .verified(child.getVerified())
                            .age(calculateAge(child.getDate_of_birth()))
                            .build();
                }).toList();
    }


    private int calculateAge(Date dateOfBirth) {
        if (dateOfBirth == null) return 0;
        return Period.between(
                dateOfBirth.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate(),
                LocalDate.now()
        ).getYears();
    }

    @Override
    public void save(Users parent) {
        usersRepository.save(parent);
    }


    public ChildDashboardResponseDto getChildById(UUID childId, CustomUserDetails userDetails) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new ResourceNotFoundException("Child topilmadi"));

        Users parent = userDetails.getUsers();
        if (parent == null) {
            throw new ResourceNotFoundException("Parent topilmadi");
        }

        LocationResponseDto location = locationsRepository
                .findLastByChildId(childId)
                .map(this::toLocationDto)
                .orElse(null);

      /*  DeviceResponseDto device = deviceRepository
                .findDeviceByChild_Id(childId)
                .map(d -> DeviceResponseDto.builder()
                        .platform(d.getPlatform())
                        .device_name(d.getDevice_name())
                        .app_version(d.getApp_version())
                        .isActive(d.isActive())
                        .build())
                .orElse(null);*/

        return ChildDashboardResponseDto.builder()
                .id(child.getId())
                .full_name(child.getFull_name())
                .avatar_url(child.getAvatar_url())
                .date_of_birth(child.getDate_of_birth())
                .verified(child.getVerified())
                .location(location)
                .geofence(null)
                .device(null)
                .build();

        // TODO geofence bilan device malumoti toliq bolishi kerak exception beradi
    }

    @Override
    public UserProfileDto getProfile(CustomUserDetails userDetails) {
        return null;
    }

    @Override
    public UserProfileDto updateProfile(UpdateProfileDto dto, CustomUserDetails userDetails) {
        return null;
    }

}