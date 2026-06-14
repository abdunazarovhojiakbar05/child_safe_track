package uz.hojiakbar.child_tracking.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import uz.hojiakbar.child_tracking.dto.auth.SendOtpRequest;
import uz.hojiakbar.child_tracking.entity.Message;
import uz.hojiakbar.child_tracking.enums.MessageStatus;
import uz.hojiakbar.child_tracking.repository.MessageRepository;
import uz.hojiakbar.child_tracking.service.MessageService;

import java.util.Random;
import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {
    private final JavaMailSender mailSender;
    private final MessageRepository messageRepository;

    public MessageServiceImpl(JavaMailSender mailSender, MessageRepository messageRepository) {
        this.mailSender = mailSender;
        this.messageRepository = messageRepository;
    }

    @Override
    @Transactional
    public String sendMessageToEmail(SendOtpRequest emailOrPhoneReq) {
        Random random = new Random();
        String code = String.format("%06d", random.nextInt(999999));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailOrPhoneReq.getEmail());
        message.setSubject("Verification Code");
        message.setText("Your verification code is: " + code + "\nThis code will expire in 5 minutes.");
        messageRepository.save(new Message(UUID.randomUUID(),emailOrPhoneReq.getEmail(),code, MessageStatus.SENT));
        mailSender.send(message);
        return code;
    }

    @Override
    public String sendSmsToPhoneNumber(SendOtpRequest emailOrPhoneReq) {
        return "";
    }
}
