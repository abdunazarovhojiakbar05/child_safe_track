package uz.hojiakbar.child_tracking.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Session;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.repository.SessionRepository;
import uz.hojiakbar.child_tracking.service.SuperAdminService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/super-admin")
@RequiredArgsConstructor
public class SuperAdminController {

    private final SuperAdminService service;

    @GetMapping("/get_parent")
    public ResponseEntity<List<Users>> getAllPArent() {
       return ResponseEntity.ok(service.getAllParent());
    }

    @GetMapping("get_child")
    public ResponseEntity<List<Child>> getAllChild() {
       return ResponseEntity.ok(service.getAllChild());
    }

    @PutMapping("/block_user/{userId}")
    public ResponseEntity<String> blockParent(@PathVariable UUID userId){
        return ResponseEntity.ok(service.blockUser(userId));

    }

    @PutMapping("/active_user/{userId}")
    public ResponseEntity<String> activeParent(@PathVariable UUID userId){
        return ResponseEntity.ok(service.activeUser(userId));

    }


    @GetMapping("/get_session")
    public ResponseEntity<List<Session>> getAllSession() {
       return ResponseEntity.ok(service.getAllSession());
    }

    private final SessionRepository sessionRepository;

    @GetMapping("/fix-sessions")
    public String fixDuplicateSessions() {
        sessionRepository.deleteOldDuplicateSessions();
        return "Duplicate sessionlar tozalandi!";
    }


}
