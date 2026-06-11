package uz.hojiakbar.child_tracking.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.hojiakbar.child_tracking.dto.auth.LoginResponseDto;
import uz.hojiakbar.child_tracking.dto.parentDto.ChildListResponseDto;
import uz.hojiakbar.child_tracking.dto.response.ErrorLogResponseDto;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.ErrorLog;
import uz.hojiakbar.child_tracking.entity.Session;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.enums.Status;
import uz.hojiakbar.child_tracking.repository.ChildRepository;
import uz.hojiakbar.child_tracking.repository.ErrorLogRepository;
import uz.hojiakbar.child_tracking.repository.SessionRepository;
import uz.hojiakbar.child_tracking.repository.UsersRepository;
import uz.hojiakbar.child_tracking.service.SuperAdminService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SuperAdminServiceImpl implements SuperAdminService {

    private final UsersRepository usersRepository;
    private final ChildRepository childRepository;
    private final SessionRepository sessionRepository;
    private final ErrorLogRepository errorLogRepository;

    @Override
    @Transactional
    public  List<LoginResponseDto> getAllParent(){

        List<Users> users = usersRepository.findAll();



      List<LoginResponseDto> list = new ArrayList<>();

      for (Users user : users) {
          list.add(new LoginResponseDto(
                  user.getId(),
                  user.getEmail(),
                  user.getRole(),
                  user.getFull_name(),
                  user.getAvatar_url(),
                  user.getPhone(),
                  user.getFcm_token(),
                  user.getStatus(),
                  user.getIsActive(),
                  user.getDate_of_birth(),
                  null,
                  null,
                  0L
          ));
      }

        return  list;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChildListResponseDto> getAllChild() {
        List<Child> child = childRepository.findAll();

        List<ChildListResponseDto> list = new ArrayList<>();
        for (Child c : child) {
            list.add(new ChildListResponseDto(
                   c.getId(),
                    c.getFull_name(),
                    c.getPhone(),
                    c.getAvatar_url(),
                    c.getDate_of_birth(),
                    c.getVerified(),
                    c.getIsActive(),
                    0

            ));
        }
        return list;
    }

    @Override
    public String blockUser(UUID userId) {
        Users user = usersRepository.findById(userId).orElseThrow();
        user.setStatus(Status.BLOCKED);
        usersRepository.save(user);
        return "User blocked successfully";
    }

    @Override
    public String activeUser(UUID userId) {
        Users user = usersRepository.findById(userId).orElseThrow();
        user.setStatus(Status.ACTIVE);
        usersRepository.save(user);
        return "User activated successfully";
    }

    @Override
    public List<Session> getAllSession() {
        return sessionRepository.findAll();
    }

    @Override
    public List<ErrorLogResponseDto> getAllExceptions() {
         List<ErrorLog> errors = errorLogRepository.findAll();

         return errors.stream()
                .map(e -> new ErrorLogResponseDto(
                        e.getId(),
                        e.getUser_id(),
                        e.getException_type(),
                        e.getError_message(),
                        e.getStatus(),
                        e.getPath(),
                        e.getCreated_at()
                ))
                .toList();
    }
}
