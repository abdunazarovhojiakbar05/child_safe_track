package uz.hojiakbar.child_tracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.hojiakbar.child_tracking.entity.Message;
import uz.hojiakbar.child_tracking.enums.MessageStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByPhoneNumber(String phoneNumber);
    Optional<Message> findByPhoneNumberAndStatus(String phoneNumber, MessageStatus status);
}
