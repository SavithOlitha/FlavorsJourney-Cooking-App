package com.example.cookingapp.repository;

import com.example.cookingapp.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByTargetUserEmailOrderByCreatedAtDesc(String targetUserEmail);
}
