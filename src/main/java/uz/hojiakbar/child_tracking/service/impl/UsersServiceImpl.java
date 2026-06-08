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
import uz.hojiakbar.child_tracking.enums.Alert_Severity;
import uz.hojiakbar.child_tracking.enums.Alert_Type;
import uz.hojiakbar.child_tracking.enums.Geofences_Type;
import uz.hojiakbar.child_tracking.enums.Status;
import uz.hojiakbar.child_tracking.exception.ResourceNotFoundException;
import uz.hojiakbar.child_tracking.repository.*;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;
import uz.hojiakbar.child_tracking.service.UsersService;

import javax.management.relation.Relation;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {


    private final UsersRepository usersRepository;
    private final ChildRepository childRepository;
    private final FamilyRelationsRepository familyRepository;
    private final DeviceRepository deviceRepository;
    private final LocationsRepository locationsRepository;
    private final Random random = new Random();



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
                            child.getAge(),
                            false,
                            location,
                            null,
                            null
                    );
                }).toList());

        SummaryResponseDto summary = new SummaryResponseDto();
        summary.setActiveChildren(relation.size());
        summary.setTotalAlertsToday(0);
        summary.setUnreadAlerts(0);

        List<ChildDashboardDto> children1 = new ArrayList<>();                                 /////
        List<GeofenceResponseDto> geofences = new ArrayList<>();                             /////
        List<ActivitySummaryResponseDto> dailyActivitySummary = new ArrayList<>();          /////
        List<AlertResponseDto> recentAlerts = new ArrayList<>();                           /////

/// -------------------------------------------------------------------------------------------
        LocationResponseDto location = new LocationResponseDto();
        Address address = new Address("Toshkent", "Sergeli");
        location.setAddress(address);
        location.setLatitude(BigDecimal.valueOf(234.3456));
        location.setLongitude(BigDecimal.valueOf(345.3456));
        location.setCreated_at(LocalDateTime.now());


/// --------------------------------------------------
        GeofenceResponseDto geofence = new GeofenceResponseDto();
        geofence.setId(UUID.randomUUID());
        geofence.setName("xali");
        geofence.setType(Geofences_Type.SAFE);
///-------------------------------------------------------------


        ActivitySummaryResponseDto activitySummary = new ActivitySummaryResponseDto();
        activitySummary.setDistance(2345D);
        activitySummary.setPlaces_visited(2342D);
        activitySummary.setScreen_time_min(341);

///-----------------------------------------------------------------------

        AlertResponseDto alert = new AlertResponseDto();
        alert.setId(UUID.randomUUID());
        alert.setTitle("pul kerak");
        alert.setType(Alert_Type.DEVICE_OFFLINE);
        alert.setCreated_at(LocalDateTime.now());
        alert.setSeverity(Alert_Severity.WARNING);


///-----------------------------------------------------------------------------------------

        children.add(new ChildDashboardDto(
                UUID.randomUUID(),
                "Kimsanov  Hoshim",
                "xali url yoq",
                true,
                34,
                false,
                location,
                geofence,
                activitySummary
        ));

        geofences.add(geofence);
        dailyActivitySummary.add(activitySummary);
        recentAlerts.add(alert);




        return new ParentDashboardResponseDto(summary, children, geofences, dailyActivitySummary, recentAlerts);
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
            throw new ResourceNotFoundException("xali farszandingiz yoq");
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
                .geofence(null) // geofence keyinroq
                .device(null)
                .build();

       // TODO geofence bilan device malumoti toliq bolishi kerak exception beradi
    }

}

