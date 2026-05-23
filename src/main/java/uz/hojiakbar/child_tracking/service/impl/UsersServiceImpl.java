package uz.hojiakbar.child_tracking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.hojiakbar.child_tracking.dto.parentDto.ChildDashboardDto;
import uz.hojiakbar.child_tracking.dto.parentDto.ChildListResponseDto;
import uz.hojiakbar.child_tracking.dto.parentDto.ParentDashboardResponseDto;
import uz.hojiakbar.child_tracking.dto.parentDto.SummaryResponseDto;
import uz.hojiakbar.child_tracking.dto.response.ActivitySummaryResponseDto;
import uz.hojiakbar.child_tracking.dto.response.AlertResponseDto;
import uz.hojiakbar.child_tracking.dto.response.GeofenceResponseDto;
import uz.hojiakbar.child_tracking.dto.response.LocationResponseDto;
import uz.hojiakbar.child_tracking.entity.Address;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Family_Relations;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.enums.Alert_Severity;
import uz.hojiakbar.child_tracking.enums.Alert_Type;
import uz.hojiakbar.child_tracking.enums.Geofences_Type;
import uz.hojiakbar.child_tracking.enums.Status;
import uz.hojiakbar.child_tracking.exception.ResourceNotFoundException;
 import uz.hojiakbar.child_tracking.repository.FamilyRelationsRepository;
import uz.hojiakbar.child_tracking.repository.UsersRepository;
import uz.hojiakbar.child_tracking.service.UsersService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {


    private final UsersRepository usersRepository;
    private final FamilyRelationsRepository familyRepository;
    private final Random random = new Random();


    @Override
    public ParentDashboardResponseDto getParentDashboard() {

        SummaryResponseDto summary = new SummaryResponseDto();
        summary.setActiveChildren(1L);
        summary.setTotalAlertsToday(3L);
        summary.setUnreadAlerts(2L);

        List<ChildDashboardDto> children = new ArrayList<>();                                 /////
        List<GeofenceResponseDto> geofences = new ArrayList<>();                             /////
        List<ActivitySummaryResponseDto> dailyActivitySummary = new ArrayList<>();          /////
        List<AlertResponseDto> recentAlerts = new ArrayList<>();                           /////

/// -------------------------------------------------------------------------------------------
        LocationResponseDto location = new LocationResponseDto();
        Address address = new Address("Toshkent", "Sergeli");
        location.setAddress(address);
        location.setLatitude(BigDecimal.valueOf(234.3456));
        location.setLongitude(BigDecimal.valueOf(345.3456));
        location.setCratedAt(LocalDateTime.now());


/// --------------------------------------------------
        GeofenceResponseDto geofence = new GeofenceResponseDto();
        geofence.setId(UUID.randomUUID());
        geofence.setName("xali");
        geofence.setType(Geofences_Type.SAFE);
///-------------------------------------------------------------


        ActivitySummaryResponseDto activitySummary = new ActivitySummaryResponseDto();
        activitySummary.setDistance(2345D);
        activitySummary.setPlacesVisited(2342D);
        activitySummary.setScreenTimeMin(341);

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

    @Override
    @Transactional(readOnly = true)
    public List<ChildListResponseDto> getChildrenByParentEmail(String email) {


        List<Family_Relations> list = familyRepository.findByParentEmail(email);

        if(list.isEmpty()){
            throw new ResourceNotFoundException("xali farszandingiz yoq");
        }

        final var verified = Status.VERIFIED;

        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getStatus() != verified){
                list.get(i).setStatus(verified);
            }

        }

        if (list.isEmpty()) {
            throw new ResourceNotFoundException("Foydalanuvchi topilmadi!");
        }

        return list.stream().map(relation -> {
            Child child = relation.getChild();
            return ChildListResponseDto.builder()
                    .id(child.getId())
                    .full_name(child.getFull_name())
                    .phone(child.getPhone())
                    .avatar_url(child.getAvatar_url())
                    .date_of_birth(child.getDate_of_birth())
                    .verified(child.getVerified())
                    .age(random.nextInt(10, 18))
                    .build();
        }).toList();

    }

    @Override
    public Users getUserByEmail(String email) {
        return usersRepository.findByEmail(email);
    }
}

