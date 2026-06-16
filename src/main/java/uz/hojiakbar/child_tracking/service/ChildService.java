package uz.hojiakbar.child_tracking.service;

import jakarta.validation.Valid;
import org.springframework.transaction.annotation.Transactional;
import uz.hojiakbar.child_tracking.dto.childDto.RegisterChildRequestDto;
import uz.hojiakbar.child_tracking.dto.childDto.RegisterResponseDto;

public interface ChildService {

    @Transactional
    RegisterResponseDto registerChildAndLink(@Valid RegisterChildRequestDto request );


}
