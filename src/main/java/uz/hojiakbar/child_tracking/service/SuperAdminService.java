package uz.hojiakbar.child_tracking.service;

import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Session;
import uz.hojiakbar.child_tracking.entity.Users;

import java.util.List;
import java.util.UUID;

public interface SuperAdminService {

    List<Users> getAllParent();

    List<Child> getAllChild();

    String blockUser(UUID userId);

    String activeUser(UUID userId);

    List<Session> getAllSession();
}
