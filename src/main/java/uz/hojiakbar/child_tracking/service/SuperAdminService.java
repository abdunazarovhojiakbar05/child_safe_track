package uz.hojiakbar.child_tracking.service;

import uz.hojiakbar.child_tracking.dto.auth.LoginResponseDto;
import uz.hojiakbar.child_tracking.dto.parentDto.ChildListResponseDto;
import uz.hojiakbar.child_tracking.dto.response.ErrorLogResponseDto;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Session;
import uz.hojiakbar.child_tracking.entity.Users;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface SuperAdminService {

    List<LoginResponseDto> getAllParent();

    List<ChildListResponseDto> getAllChild();

    String blockUser(UUID userId);

    String activeUser(UUID userId);

    Map<UUID, String> getAllSession();

    List<ErrorLogResponseDto> getAllExceptions();
}
