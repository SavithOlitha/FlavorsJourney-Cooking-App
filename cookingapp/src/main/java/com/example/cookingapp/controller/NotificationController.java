package com.example.cookingapp.controller;

import com.example.cookingapp.model.Notification;
import com.example.cookingapp.model.User;
import com.example.cookingapp.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     * Returns notifications for the currently authenticated user.
     */
    @GetMapping
    public ResponseEntity<?> getMyNotifications() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        List<Notification> notifications = notificationRepository.findByTargetUserEmailOrderByCreatedAtDesc(currentUser.getEmail());
        return ResponseEntity.ok(notifications);
    }
}
