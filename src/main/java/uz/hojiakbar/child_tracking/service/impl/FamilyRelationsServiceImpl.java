package uz.hojiakbar.child_tracking.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.hojiakbar.child_tracking.dto.auth.EmailDto;
import uz.hojiakbar.child_tracking.dto.childDto.RegisterChildRequestDto;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Family_Relations;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.enums.Relation_Type;
import uz.hojiakbar.child_tracking.enums.Status;
import uz.hojiakbar.child_tracking.repository.FamilyRelationsRepository;
import uz.hojiakbar.child_tracking.repository.UsersRepository;
import uz.hojiakbar.child_tracking.service.FamilyRelationsService;

import java.security.SecureRandom;
import java.util.NoSuchElementException;


@Service
@RequiredArgsConstructor
public class FamilyRelationsServiceImpl implements FamilyRelationsService {

    private final FamilyRelationsRepository familyRepository;
    private final UsersRepository UsersRepository;


    @Override
    public String generateInviteCode(EmailDto email) {
        String code = generateRandomCode();
        Users user = UsersRepository.findByEmail(email.getEmail());

        if (user == null) {
            throw new NoSuchElementException("Foydalanuvchi topilmadi!");
        }
        Family_Relations relation = Family_Relations.builder()
                .parent(user)
                .invite_code(code)
                .status(Status.PENDING)
                .type(Relation_Type.FATHER)
                .build();
        familyRepository.save(relation);

        return code;
    }

    @Override
    public Users getParentByInviteCode(String inviteCode) {
        return familyRepository.findByInvite_code(inviteCode)
                .map(Family_Relations::getParent)
                .orElseThrow(() -> new NoSuchElementException("Invite code topilmadi!"));
    }

    @Override
    public Family_Relations findByInviteCode(String inviteCode) {
        return familyRepository.findByInvite_code(inviteCode)
                .orElseThrow(() -> new NoSuchElementException("Invite code topilmadi!"));
    }

    @Override
    public Family_Relations createRelation(RegisterChildRequestDto request) {
        Family_Relations relation = findByInviteCode(request.getInviteCode());
        relation.setStatus(Status.ACCEPTED);
        return familyRepository.save(relation);
    }

    @Override
    public void updateRelationWithChild(Family_Relations relation, Child child) {
        relation.setChild(child);
        familyRepository.save(relation);
    }


    private String generateRandomCode() {
        String codeStr = "ABDEFGHIJKLMNOPQRSTUVXYWZ1234567890";
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(codeStr.length());
            code.append(codeStr.charAt(index));
        }

        return code.toString();
    }

}
