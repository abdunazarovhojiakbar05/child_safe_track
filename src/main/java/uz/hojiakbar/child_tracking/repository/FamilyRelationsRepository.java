package uz.hojiakbar.child_tracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.hojiakbar.child_tracking.entity.Family_Relations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FamilyRelationsRepository extends JpaRepository<Family_Relations, UUID> {

    List<Family_Relations> findByParentEmail(String email);

    @Query("SELECT f FROM family_relations f WHERE f.child.email = :email")
    Optional<Family_Relations> findByChildEmail(@Param("email") String email);

    @Query("SELECT f FROM family_relations f WHERE f.child.email = :email")
    List<Family_Relations> findAllByChildEmail(@Param("email") String email);
 }

