package uz.hojiakbar.child_tracking.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.service.SuperAdminService;

import java.util.List;

@RestController
@RequestMapping("/api/super-admin")
@RequiredArgsConstructor
public class SuperAdminController {

    private final SuperAdminService usersService;

    @GetMapping("/get_parent")
    public ResponseEntity<List<Users>> getAllPArent() {
       return ResponseEntity.ok(usersService.getAllParent());
    }

    @GetMapping("get_child")
    public ResponseEntity<List<Child>> getAllChild() {
       return ResponseEntity.ok(usersService.getAllChild());
    }


}
