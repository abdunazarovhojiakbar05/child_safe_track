package uz.hojiakbar.child_tracking.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.repository.ChildRepository;
import uz.hojiakbar.child_tracking.repository.UsersRepository;
import uz.hojiakbar.child_tracking.service.SuperAdminService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SuperAdminServiceImpl implements SuperAdminService {

    private final UsersRepository usersRepository;
    private final ChildRepository childRepository;

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
}
