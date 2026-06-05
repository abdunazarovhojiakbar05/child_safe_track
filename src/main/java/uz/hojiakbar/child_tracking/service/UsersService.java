package uz.hojiakbar.child_tracking.service;

import org.springframework.http.ResponseEntity;
import uz.hojiakbar.child_tracking.dto.childDto.ChildResponseDto;
import uz.hojiakbar.child_tracking.dto.parentDto.ChildDashboardResponseDto;
import uz.hojiakbar.child_tracking.dto.parentDto.ChildListResponseDto;
import uz.hojiakbar.child_tracking.dto.parentDto.ParentDashboardResponseDto;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;

import java.util.List;
import java.util.UUID;

public interface UsersService {

    ParentDashboardResponseDto getParentDashboard(String userDetails);

    List< ChildListResponseDto>   getChildrenByParentEmail(String email);

    void save(Users parent);

    ChildDashboardResponseDto getChildById(UUID childId, CustomUserDetails userDetails);

 }
