package uz.hojiakbar.child_tracking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.hojiakbar.child_tracking.dto.childDto.ChildRequestDto;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Family_Relations;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.enums.Status;
import uz.hojiakbar.child_tracking.repository.ChildRepository;
import uz.hojiakbar.child_tracking.repository.FamilyRelationsRepository;
import uz.hojiakbar.child_tracking.repository.UsersRepository;
import uz.hojiakbar.child_tracking.service.FamilyRelationsService;
import uz.hojiakbar.child_tracking.util.JwtUtils;


@Service
@RequiredArgsConstructor
public class FamilyRelationsServiceImpl implements FamilyRelationsService {

    private final ChildRepository childRepository;
    private final FamilyRelationsRepository familyRelationsRepository;
    private final JwtUtils jwtUtils;
    private final UsersRepository parentRepository;


    @Override
    public String generateInviteCode(ChildRequestDto dto, String parentEmail) {

        Child existingChild = childRepository.findByEmail(dto.getEmail());
        if (existingChild != null) {
            throw new RuntimeException("Bu email bilan bola allaqachon mavjud!");
        }
        Users parent = parentRepository.findByEmail(parentEmail);

        Child child = Child.builder()
                .email(dto.getEmail())
                .full_name(dto.getName())
                .verified(Status.NOT_VERIFIED)
                .isActive(false)
                .build();
        childRepository.save(child);

        String childToken = jwtUtils.generateToken(child.getEmail());

        Family_Relations relations = Family_Relations.builder()
                .child(child)
                .parent(parent)
                .build();
        familyRelationsRepository.save(relations);

        return childToken;
    }
}
