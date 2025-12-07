package com.example.tubesrpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.tubesrpl.model.Matkul;
import com.example.tubesrpl.model.Semester;
import com.example.tubesrpl.model.User;
import com.example.tubesrpl.repository.MatkulRepository;
import com.example.tubesrpl.repository.SemesterRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@SessionAttributes("user")
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    SemesterRepository semesterRepository;

    @Autowired
    MatkulRepository matkulRepository;

    @ModelAttribute("user") //supaya ga nerima parameter HttpSesison berkali kali
    public User userSession(HttpSession session) {
        return (User) session.getAttribute("user");
    }
    
    @GetMapping("/home")
    public String homeAdminView(){
        return "Admin/homeAdmin";
    }

    @GetMapping("/semesters")
    public String semestersAdminView(Model model){
        List<Semester> semesterList = semesterRepository.findAll();
        model.addAttribute("semesterList", semesterList); //matkul list karena menggunakan template yang sama dengan matkul
        return "Admin/semester";
    }

    @GetMapping("/semesters/{idSemester}/courses")
    public String coursesAdminView(@PathVariable Long idSemester, Model model){
        List<Matkul> matkulList = matkulRepository.findAllBySemester(idSemester);
        model.addAttribute("matkulList", matkulList);
        return "Admin/matkul";
    }
}
