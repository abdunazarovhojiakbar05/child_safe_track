package uz.hojiakbar.child_tracking.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.hojiakbar.child_tracking.dto.auth.LoginResponseDto;
import uz.hojiakbar.child_tracking.dto.parentDto.ChildListResponseDto;
import uz.hojiakbar.child_tracking.dto.response.ErrorLogResponseDto;
import uz.hojiakbar.child_tracking.entity.Child;
import uz.hojiakbar.child_tracking.entity.Session;
import uz.hojiakbar.child_tracking.entity.Users;
import uz.hojiakbar.child_tracking.repository.SessionRepository;
import uz.hojiakbar.child_tracking.service.SuperAdminService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/super-admin")
@RequiredArgsConstructor
public class SuperAdminController {

    private final SuperAdminService service;
    private final SessionRepository sessionRepository;

    @GetMapping("/get_parent")
    public ResponseEntity<List<LoginResponseDto>> getAllPArent() {
       return ResponseEntity.ok(service.getAllParent());
    }

    @GetMapping("get_child")
    public ResponseEntity<List<ChildListResponseDto>> getAllChild() {
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
    public ResponseEntity<Map<UUID, String>> getAllSession() {
       return ResponseEntity.ok(service.getAllSession());
    }


    @GetMapping("/get_all_exceptions")
    public ResponseEntity<List<ErrorLogResponseDto>> getAllExceptions() {
        return ResponseEntity.ok(service.getAllExceptions());
    }


    @DeleteMapping("/clean-duplicate-sessions")
    public ResponseEntity<String> cleanDuplicateSessions() {
        sessionRepository.deleteOldDuplicateSessions();
        return ResponseEntity.ok("Dublikat sessionlar tozalandi!");
    }

}
