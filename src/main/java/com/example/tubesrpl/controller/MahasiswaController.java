package com.example.tubesrpl.controller; // Pastikan package sesuai

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.tubesrpl.model.TahapTubes; // GANTI Phase jadi TahapTubes
import com.example.tubesrpl.model.Tubes;
import com.example.tubesrpl.model.User;
import com.example.tubesrpl.repository.TahapRepository; // GANTI PhaseRepository jadi TahapRepository
import com.example.tubesrpl.repository.TubesRepository;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@SessionAttributes("user")
@RequestMapping("/mahasiswa")
public class MahasiswaController {

    @Autowired 
    private TubesRepository tubesRepository;

    @Autowired
    private TahapRepository tahapRepository; // Rename

    @ModelAttribute("user") 
    public User userSession(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    @GetMapping("/home")
    public String homeMhsView(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        // Sementara hardcode semester 1
        Long semesterId = 1L;

        List<Tubes> tubes = tubesRepository.findAllBySemester(semesterId);
        
        // Sekarang pakai TahapRepository
        List<TahapTubes> phases = tahapRepository.findAllBySemester(semesterId);

        model.addAttribute("tubeslist", tubes); 
        model.addAttribute("phaselist", phases);
        model.addAttribute("user", user);
        
        return "Mahasiswa/course-home";
    }
}
