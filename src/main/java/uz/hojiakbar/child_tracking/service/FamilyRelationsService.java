package uz.hojiakbar.child_tracking.service;

import uz.hojiakbar.child_tracking.dto.auth.EmailDto;
import uz.hojiakbar.child_tracking.dto.childDto.RegisterChildRequestDto;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Family_Relations;
import uz.hojiakbar.child_tracking.entity.Users;

public interface FamilyRelationsService {

    String generateInviteCode(EmailDto email);


    Users getParentByInviteCode(String inviteCode);

    Family_Relations findByInviteCode(String inviteCode);

    Family_Relations createRelation(RegisterChildRequestDto request);

    void updateRelationWithChild(Family_Relations relation, Child child);
}
