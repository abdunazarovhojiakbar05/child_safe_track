package uz.hojiakbar.child_tracking.service;


import uz.hojiakbar.child_tracking.dto.auth.SendOtpRequest;

public interface MessageService {
    String sendMessageToEmail(SendOtpRequest emailOrPhoneReq);

    String sendSmsToPhoneNumber(SendOtpRequest emailOrPhoneReq);
}
