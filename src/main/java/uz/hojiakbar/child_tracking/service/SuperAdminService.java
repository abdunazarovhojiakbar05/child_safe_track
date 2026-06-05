package uz.hojiakbar.child_tracking.service;

import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Users;

import java.util.List;

public interface SuperAdminService {

    List<Users> getAllParent();

    List<Child> getAllChild();
}
