package com.example.tubesrpl.controller;

import com.example.tubesrpl.repository.MatkulRepository;
import com.example.tubesrpl.model.Matkul;
import com.example.tubesrpl.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MatkulController {

    @Autowired
    private MatkulRepository matkulRepository;

    // pake localhost:8080/matkul
    @GetMapping("/mahasiswa/course")
    public String viewCoursePage(HttpSession session, Model model) {
        // ambil user dari session saat ini
        User user = (User) session.getAttribute("user");

        // redirect ke halaman login kalo belum login (user nya null)
        if (user == null) {
            return "redirect:/login";
        }

        // ambil data Matkul user tersebut
        List<Matkul> matkulList = matkulRepository.findAllByUserId(user.getId());

        // kirim data ke HTML
        model.addAttribute("user", user);
        model.addAttribute("matkulList", matkulList);

        return "Mahasiswa/course-home"; 
    }
}