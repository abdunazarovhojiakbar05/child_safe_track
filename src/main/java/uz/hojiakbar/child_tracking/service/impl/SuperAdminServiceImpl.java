package uz.hojiakbar.child_tracking.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Session;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.enums.Status;
import uz.hojiakbar.child_tracking.repository.ChildRepository;
import uz.hojiakbar.child_tracking.repository.SessionRepository;
import uz.hojiakbar.child_tracking.repository.UsersRepository;
import uz.hojiakbar.child_tracking.service.SuperAdminService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SuperAdminServiceImpl implements SuperAdminService {

    private final UsersRepository usersRepository;
    private final ChildRepository childRepository;
    private final SessionRepository sessionRepository;

    @Override
    @Transactional
    public List<Users> getAllParent(){
        return usersRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Child> getAllChild() {
        return childRepository.findAll();
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
}
