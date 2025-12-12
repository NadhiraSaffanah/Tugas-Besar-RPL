package com.example.tubesrpl.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.tubesrpl.data.User;
import com.example.tubesrpl.model.Matkul;
import com.example.tubesrpl.model.TahapTubes;
import com.example.tubesrpl.model.Tubes;
import com.example.tubesrpl.data.Tubes;
import com.example.tubesrpl.repository.MatkulRepository;
import com.example.tubesrpl.repository.TahapRepository;
import com.example.tubesrpl.repository.TubesRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class MahasiswaController {
    @Autowired
    private MatkulRepository matkulRepository;

    @Autowired
    private TubesRepository tubesRepository; // BARU

    @Autowired
    private TahapRepository tahapRepository; // BARU

    @GetMapping()
    private String mahasiswaHome(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";
        model.addAttribute("mahasiswa", user);

        return "mahasiswa/homeMhs";
    }
    
    @GetMapping("/mahasiswa/profile")
    public String mahasiswaProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";
        model.addAttribute("mahasiswa", user);

        return "#";
    }
    
    // ROUTING UNTUK COURSE (BARU DITAMBAHIN BACKEND)
    @GetMapping("/mahasiswa/course")
    public String courseHome(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        // kalo belum login, redirect balik ke halaman login
        if (user == null) {
            return "redirect:/login";
        }

        // Ambil Data Matkul milik Dosen tersebut
        List<Matkul> courses = matkulRepository.findAllByUserId(user.getId());

        model.addAttribute("dosen", user); // Di HTML dipanggil ${dosen.nama}
        model.addAttribute("listCourses", courses); // Di HTML dipanggil ${listCourses}

        return "#";
    }
    
    // BARU
    @GetMapping("mahasiswa/course/details")
    public String courseDetails(@RequestParam(name = "id") Long matkulId, HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        // ambil info header (selalu ada walaupun misal belum ada tubesnya)
        // Isinya: nama_matkul, start_date, end_date
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
            
            List<TahapTubes> listTahap = tahapRepository.findAllByTubesId(tubes.getId());
            model.addAttribute("listTahap", listTahap);
        } else {
            model.addAttribute("tubes", null);
            model.addAttribute("listTahap", null);
        }

        model.addAttribute("selectedMatkulId", matkulId);
        model.addAttribute("mahasiswa", user);

        return "mahasiswa/course-details";
    }
}
