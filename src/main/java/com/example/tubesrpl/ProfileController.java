package com.example.tubesrpl;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

@Controller 
public class ProfileController {

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
    @PostMapping("/upload-photo")
    @ResponseBody
    public ResponseEntity<?> uploadPhoto(@RequestParam("file") MultipartFile file) {

        // TODO: Save file to database / folder
        System.out.println("Received photo: " + file.getOriginalFilename());

        return ResponseEntity.ok("Photo uploaded");
    }

    // ========== CHANGE PASSWORD ==========
    @PostMapping("/change-password")
    @ResponseBody
    public ResponseEntity<?> changePassword(@RequestBody PasswordRequest req) {

        // TODO: Validate and save new password
        System.out.println("Old: " + req.oldPassword);
        System.out.println("New: " + req.newPassword);

        return ResponseEntity.ok("Password updated");
    }

    // Request body model
    public static class PasswordRequest {
        public String oldPassword;
        public String newPassword;
    }
}