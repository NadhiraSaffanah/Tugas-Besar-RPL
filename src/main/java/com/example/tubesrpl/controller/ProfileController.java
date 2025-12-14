package com.example.tubesrpl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.tubesrpl.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller 
public class ProfileController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "profile-page";
    }

    // ========== UPLOAD PHOTO ==========
    @PostMapping("/api/profile/upload-photo")
    @ResponseBody
    public ResponseEntity<?> uploadPhoto(@RequestParam("file") MultipartFile file, HttpSession session) {
 
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.status(401).body("User not authenticated");
            }

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            // Create uploads/profile directory if it doesn't exist
            String uploadDir = "src/main/resources/static/uploads/profile/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate unique filename: user_id_timestamp_originalname
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".") 
                ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
                : ".jpg";
            String uniqueFilename = user.getId() + "_" + System.currentTimeMillis() + extension;

            // Save file to disk
            Path targetPath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), targetPath);

            // Update database
            String sql = "UPDATE users SET foto_profil = ? WHERE id = ?";
            jdbcTemplate.update(sql, uniqueFilename, user.getId());

            // Update session user object
            user.setFotoProfil(uniqueFilename);
            session.setAttribute("user", user);

            System.out.println("Photo saved: " + uniqueFilename);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to save file: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }

        return ResponseEntity.ok("Photo uploaded");
    }

    // ========== CHANGE PASSWORD ==========
    @PostMapping("/api/profile/change-password")
    @ResponseBody
    public ResponseEntity<?> changePassword(@RequestBody PasswordRequest req, HttpSession session) {
 
        try {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return ResponseEntity.status(401).body("User not authenticated");
            }

            // Validate input
            if (req.oldPassword == null || req.oldPassword.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Old password is required");
            }
            if (req.newPassword == null || req.newPassword.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("New password is required");
            }
            if (req.confirmPassword == null || req.confirmPassword.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Confirm password is required");
            }

            // Validate that new password and confirm password match
            if (!req.newPassword.equals(req.confirmPassword)) {
                return ResponseEntity.badRequest().body("New password and confirm password do not match");
            }

            // Get current password from database
            String sql = "SELECT password_hash FROM users WHERE id = ?";
            String currentPassword = jdbcTemplate.queryForObject(sql, String.class, user.getId());

            // Compare old password with current password
            if (!currentPassword.equals(req.oldPassword)) {
                return ResponseEntity.status(400).body("Old password is incorrect");
            }

            // Update password in database
            String updateSql = "UPDATE users SET password_hash = ? WHERE id = ?";
            jdbcTemplate.update(updateSql, req.newPassword, user.getId());

            // Update session user object
            user.setPassword(req.newPassword);
            session.setAttribute("user", user);

            System.out.println("Password updated for user: " + user.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error updating password: " + e.getMessage());
        }

        return ResponseEntity.ok("Password updated");
    }

    // Request body model
    public static class PasswordRequest {
        public String oldPassword;
        public String newPassword;
        public String confirmPassword;
    }
}