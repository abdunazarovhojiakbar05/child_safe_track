package uz.hojiakbar.child_tracking.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Family_Relations;
import uz.hojiakbar.child_tracking.entity.Users;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FamilyRelationsRepository extends JpaRepository<Family_Relations, UUID> {

    @org.springframework.data.jpa.repository.Query("select f from family_relations f where f.invite_code = :inviteCode")
    Optional<Family_Relations> findByInvite_code(String inviteCode);

    List<Family_Relations> findByParentEmail(String email);

    @Query("SELECT fr.child FROM family_relations fr JOIN fr.child WHERE fr.parent.email = :email")
    List<Child> findChildrenByParentEmail(@Param("email") String email);
}
