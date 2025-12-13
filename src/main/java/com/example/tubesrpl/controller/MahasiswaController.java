package com.example.tubesrpl.controller; // Pastikan package sesuai

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.tubesrpl.model.TahapTubes; // GANTI Phase jadi TahapTubes
import com.example.tubesrpl.model.Tubes;
import com.example.tubesrpl.model.User;
import com.example.tubesrpl.repository.MatkulRepository;
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
    MatkulRepository matkulRepository;

    @Autowired
    private TahapRepository tahapRepository; // Rename

    @ModelAttribute("user") 
    public User userSession(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    @GetMapping("/home")
    public String homeMhsView(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";
        // Sementara hardcode semester 1
        Long semesterId = 1L;

        List<Tubes> tubes = tubesRepository.findAllBySemester(semesterId);

        // Sekarang pakai TahapRepository
        List<TahapTubes> phases = tahapRepository.findAllBySemester(semesterId);

        model.addAttribute("tubeslist", tubes);
        model.addAttribute("phaselist", phases);
        model.addAttribute("user", user);

        return "Mahasiswa/homeMhs";
    }
    
    @GetMapping("/course-details")
    public String courseDetailsMhs(@RequestParam(name = "id") Long matkulId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        boolean isLocked = false;

        // BARU, kalau bukan dosen redirect ke login
        if (user == null || !"mahasiswa".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        Map<String, Object> headerInfo = matkulRepository.findHeaderInfo(matkulId);

        // konversi java.sql.Date -> java.time.LocalDate 
        if (headerInfo.get("start_date") != null) {
            java.sql.Date sqlDate = (java.sql.Date) headerInfo.get("start_date");
            headerInfo.put("start_date", sqlDate.toLocalDate());
        }
        
        if (headerInfo.get("end_date") != null) {
            java.sql.Date sqlDate = (java.sql.Date) headerInfo.get("end_date");
            headerInfo.put("end_date", sqlDate.toLocalDate());
        }
        model.addAttribute("header", headerInfo);

        // ambil tubes (bisa null)
        Optional<Tubes> tubesOpt = tubesRepository.findAllByMatkulId(matkulId);

        if (tubesOpt.isPresent()) {
            Tubes tubes = tubesOpt.get();
            model.addAttribute("tubes", tubes);

            isLocked = tubes.isLocked();
            
            List<TahapTubes> listTahap = tahapRepository.findAllByTubesId(tubes.getId());
            model.addAttribute("listTahap", listTahap);
        } else {
            model.addAttribute("tubes", null);
            model.addAttribute("listTahap", null);
        }

        model.addAttribute("selectedMatkulId", matkulId);
        model.addAttribute("user", user);
        model.addAttribute("isGroupLocked", isLocked);

        return "mahasiswa/course-details";
    }
}
