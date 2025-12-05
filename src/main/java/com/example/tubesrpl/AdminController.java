package com.example.tubesrpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.tubesrpl.model.Semester;
import com.example.tubesrpl.model.User;
import com.example.tubesrpl.repository.SemesterRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("user")
public class AdminController {
    @Autowired
    SemesterRepository semesterRepository;

    @ModelAttribute("user") //supaya ga nerima parameter HttpSesison berkali kali
    public User userSession(HttpSession session) {
        return (User) session.getAttribute("user");
    }
    
    @GetMapping("/home-admin")
    public String homeAdminView(){
        return "Admin/homeAdmin";
    }

    @GetMapping("/course")
    public String coursesAdminView(Model model){
        List<Semester> semesterList = semesterRepository.findAll();
        model.addAttribute("matkulList", semesterList); //matkul list karena menggunakan template yang sama dengan matkul
        return "Admin/semester";
    }
}
