package uz.hojiakbar.child_tracking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.hojiakbar.child_tracking.dto.childDto.RegisterChildRequestDto;
import uz.hojiakbar.child_tracking.dto.childDto.RegisterResponseDto;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Family_Relations;
import uz.hojiakbar.child_tracking.entity.Session;
import uz.hojiakbar.child_tracking.enums.Status;
import uz.hojiakbar.child_tracking.repository.ChildRepository;
import uz.hojiakbar.child_tracking.repository.FamilyRelationsRepository;
import uz.hojiakbar.child_tracking.repository.SessionRepository;
import uz.hojiakbar.child_tracking.service.ChildService;
import uz.hojiakbar.child_tracking.util.JwtUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ChildServiceImpl implements ChildService {

    private final FamilyRelationsRepository familyRelationsRepository;
    private final SessionRepository sessionRepository;
    private final ChildRepository childRepository;
    private final JwtUtils jwtUtils;

    @Transactional
    @Override
    public RegisterResponseDto registerChildAndLink(RegisterChildRequestDto request) {

        String email = request.getEmail();

         Child child = childRepository.findByEmail(email);
        if (child == null) {
            throw new RuntimeException("Child topilmadi: " + email);
        }

         Family_Relations relations = familyRelationsRepository
                .findByChildEmail(email)
                .orElseThrow(() -> new RuntimeException("Family_Relations topilmadi: " + email));

         if (relations.getParent() == null) {
            throw new RuntimeException("Parent bog'lanmagan! generateInviteCode da xato bor.");
        }

         child.setPhone(request.getPhone());
        child.setGender(request.getGender());
        child.setDate_of_birth(request.getDatedOfBirth());
        child.setVerified(Status.ACTIVE);
        child.setIsActive(true);


        if (child.getParents() == null) {
            child.setParents(new ArrayList<>());
        }

        if (!child.getParents().contains(relations.getParent())) {
            child.getParents().add(relations.getParent());
        }
        childRepository.save(child);

         String accessToken = jwtUtils.generateToken(child.getEmail());
        String refreshToken = jwtUtils.generateRefreshToken(child.getEmail());

         Session session = Session.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .child(child)
                .ipAddress("unknown")
                .userAgent("unknown")
                .deviceId(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();
        sessionRepository.save(session);

        return RegisterResponseDto.builder()
                .child_id(child.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .parent_id(relations.getParent().getId())
                .build();
    }
}
