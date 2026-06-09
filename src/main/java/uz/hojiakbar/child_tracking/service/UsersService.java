package uz.hojiakbar.child_tracking.service;

import uz.hojiakbar.child_tracking.dto.parentDto.*;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;

import java.util.List;
import java.util.UUID;

public interface UsersService {

    ParentDashboardResponseDto getParentDashboard(String userDetails);

    List< ChildListResponseDto>   getChildrenByParentEmail(String email);

    void save(Users parent);

    ChildDashboardResponseDto getChildById(UUID childId, CustomUserDetails userDetails);

    UserProfileDto getProfile(CustomUserDetails userDetails);

    UserProfileDto updateProfile(UpdateProfileDto dto, CustomUserDetails userDetails);
}
