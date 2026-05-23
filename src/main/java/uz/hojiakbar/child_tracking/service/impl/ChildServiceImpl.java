package uz.hojiakbar.child_tracking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.hojiakbar.child_tracking.dto.childDto.ChildRequestDto;
import uz.hojiakbar.child_tracking.dto.childDto.RegisterChildRequestDto;
import uz.hojiakbar.child_tracking.dto.childDto.RegisterResponseDto;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Family_Relations;
import uz.hojiakbar.child_tracking.entity.Session;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.enums.Status;
import uz.hojiakbar.child_tracking.repository.ChildRepository;
import uz.hojiakbar.child_tracking.repository.SessionRepository;
import uz.hojiakbar.child_tracking.repository.UsersRepository;
import uz.hojiakbar.child_tracking.service.ChildService;
import uz.hojiakbar.child_tracking.service.FamilyRelationsService;
import uz.hojiakbar.child_tracking.util.JwtUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ChildServiceImpl implements ChildService {

    private final FamilyRelationsService familyRelationsService;
    private final SessionRepository sessionRepository;
    private final UsersRepository usersRepository;
    private final ChildRepository ChildRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;


    @Override
    @Transactional(readOnly = true)
    public String verifyCode(ChildRequestDto dto) {
        var relation = familyRelationsService.findByInviteCode(dto.getInvite_code());
        if (relation == null) {
            throw new RuntimeException("Invite code not found");
        }

        Users parent = relation.getParent();
        if (parent == null) {
            throw new RuntimeException("Parent not found for this code");
        }

        return parent.getFull_name();
    }


    @Transactional
    @Override
    public RegisterResponseDto registerChildAndLink(RegisterChildRequestDto request) {
        Family_Relations relations = familyRelationsService.createRelation(request);


        if (relations == null) {
            throw new RuntimeException("relation not found ");
        }

        if (request.getInviteCode() == null) {
            throw new RuntimeException("inviteCode not found ");
        }

        Child child = Child.builder()
                .full_name(request.getFullName())
                .isActive(true)
                .date_of_birth(request.getDatedOfBirth())
                .gender(request.getGender())
                .updated_at(LocalDateTime.now())
                .created_at(LocalDateTime.now())
                .verified(Status.NOT_VERIFIED)
                .phone(request.getPhone())
                .parents(List.of(usersRepository.findById(relations.getParent().getId()).orElseThrow()))
                .password_hash(passwordEncoder.encode(request.getPassword()))
                .build();

        Child savedChild = ChildRepository.save(child);
        familyRelationsService.updateRelationWithChild(relations, savedChild);

        String accessToken = jwtUtils.generateToken(savedChild.getPhone());
        String refreshToken = jwtUtils.generateRefreshToken(savedChild.getPhone());

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
                .child_id(savedChild.getId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .parent_id(relations.getParent().getId())
                .build();
    }
}
