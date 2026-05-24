package uz.hojiakbar.child_tracking.service;

import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import uz.hojiakbar.child_tracking.dto.childDto.ChildRequestDto;
import uz.hojiakbar.child_tracking.dto.childDto.ChildResponseDto;
import uz.hojiakbar.child_tracking.dto.childDto.RegisterChildRequestDto;
import uz.hojiakbar.child_tracking.dto.childDto.RegisterResponseDto;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Users;

public interface ChildService {

    @Transactional
    RegisterResponseDto registerChildAndLink(@Valid RegisterChildRequestDto request );


}
