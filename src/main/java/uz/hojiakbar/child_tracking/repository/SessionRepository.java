package uz.hojiakbar.child_tracking.repository;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.hojiakbar.child_tracking.entity.Session;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {
    Optional<Session> findByRefreshToken(String refreshToken);

    @Transactional
    void deleteByRefreshToken(String refreshToken);

    Session findSessionByUser_Email(String email);

    Session findSessionById(UUID id);

    Optional<Session> findByAccessToken(String accessToken);

   @Modifying
@Transactional
@Query(value = """
    DELETE FROM sessions
    WHERE id NOT IN (
            SELECT latest_id FROM (
                    SELECT DISTINCT ON (user_id) id as latest_id
    FROM sessions
    ORDER BY user_id, created_at DESC
        ) as sub
    )   
            """, nativeQuery = true)
void deleteOldDuplicateSessions();

    Session findSessionByChild_Email(@NotBlank @Email String email);
}
