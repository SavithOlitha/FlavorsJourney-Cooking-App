package com.example.cookingapp.controller;

import com.example.cookingapp.model.NotificationType;
import com.example.cookingapp.model.User;
import com.example.cookingapp.repository.UserRepository;
import com.example.cookingapp.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")

@RestController
@RequestMapping("/api/users")
public class FollowController {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    // Follow a user by ID
    @PostMapping("/follow/{id}")
    public ResponseEntity<?> followUser(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();

        Optional<User> targetOpt = userRepository.findById(id);
        if (targetOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User targetUser = targetOpt.get();

        if (currentUser.getId().equals(targetUser.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You cannot follow yourself");
        }

        if (currentUser.getFollowing().contains(targetUser)) {
            return ResponseEntity.badRequest().body("Already following the user");
        }

        currentUser.getFollowing().add(targetUser);
        targetUser.getFollowers().add(currentUser);

        userRepository.save(currentUser);
        userRepository.save(targetUser);

        // Create a follow notification for the target user
        notificationService.createNotification(
                NotificationType.FOLLOW,
                targetUser.getEmail(),
                currentUser.getEmail(),
                currentUser.getUsername() + " started following you.",
                null,
                null
        );

        return ResponseEntity.ok("User followed successfully");
    }


    // Unfollow a user by ID
    @PostMapping("/unfollow/{id}")
    public ResponseEntity<?> unfollowUser(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();

        Optional<User> targetOpt = userRepository.findById(id);
        if (targetOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        User targetUser = targetOpt.get();

        if (!currentUser.getFollowing().contains(targetUser)) {
            return ResponseEntity.badRequest().body("You are not following this user");
        }

        currentUser.getFollowing().remove(targetUser);
        targetUser.getFollowers().remove(currentUser);

        userRepository.save(currentUser);
        userRepository.save(targetUser);

        // (Optionally, create a notification for unfollow if needed, typically not notified)
        // notificationService.createNotification(...);
        notificationService.createNotification(
                NotificationType.UNFOLLOW,
                targetUser.getEmail(),
                currentUser.getEmail(),
                currentUser.getUsername() + " unfollowed you.",
                null,
                null
        );
        return ResponseEntity.ok("User unfollowed successfully");
    }


    // Get list of users the given user is following
    @GetMapping("/{id}/following")
    public ResponseEntity<?> getFollowing(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.ok(userOpt.get().getFollowing());
    }

    // Get list of followers for a given user
    @GetMapping("/{id}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.ok(userOpt.get().getFollowers());
    }
}
