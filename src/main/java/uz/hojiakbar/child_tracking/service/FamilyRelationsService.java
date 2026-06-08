package uz.hojiakbar.child_tracking.service;

import uz.hojiakbar.child_tracking.dto.auth.EmailDto;
import uz.hojiakbar.child_tracking.dto.childDto.ChildRequestDto;
import uz.hojiakbar.child_tracking.dto.childDto.RegisterChildRequestDto;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Family_Relations;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.security.CustomUserDetails;

public interface FamilyRelationsService {

    String addChild(ChildRequestDto dto, String email);

 }
