package com.example.tubesrpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.tubesrpl.model.Tubes;
import com.example.tubesrpl.model.User;
import com.example.tubesrpl.repository.TubesRepository;

import jakarta.servlet.http.HttpSession;


@Controller
@SessionAttributes("user")
@RequestMapping("/dosen")
public class DosenController {
    @Autowired 
    private TubesRepository tubesRepository;

    @ModelAttribute("user") //supaya ga nerima parameter HttpSesison berkali kali
    public User userSession(HttpSession session) {
        return (User) session.getAttribute("user");
    }
    
    @GetMapping("/home")
    public String homeDosenView(Model model){
        List<Tubes> tubes = tubesRepository.findAllBySemester((long) 1);
        model.addAttribute("tubeslist", tubes); //sementara ambil semester 1 doang
        return "Dosen/homeDosen";
    }

    @GetMapping("/course")
    public String courseDosenView(){
        return "matkul";
    }
}
