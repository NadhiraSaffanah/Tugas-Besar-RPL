package com.example.tubesrpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.tubesrpl.model.Phase;
import com.example.tubesrpl.model.Tubes;
import com.example.tubesrpl.model.User;
import com.example.tubesrpl.repository.TubesRepository;

import jakarta.servlet.http.HttpSession;
import com.example.tubesrpl.repository.PhaseRepository;

@Controller
@SessionAttributes("user")
@RequestMapping("/mahasiswa")
public class MahasiswaController {
    @Autowired 
    private TubesRepository tubesRepository;

    @Autowired
    private PhaseRepository phaseRepository;

    @ModelAttribute("user") //supaya ga nerima parameter HttpSesison berkali kali
    public User userSession(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    @GetMapping("/home")
    public String homeMhsView(Model model){
        System.out.println(phaseRepository);
        List<Tubes> tubes = tubesRepository.findAllBySemester((long) 1);
        List<Phase> phase = phaseRepository.findAllBySemester((long) 1);
       
        model.addAttribute("tubeslist", tubes); //sementara ambil semester 1 doang
        model.addAttribute("phaselist", phase);
        return "Mahasiswa/homeMhs";
    }

    @GetMapping("/course")
    public String courseMhsView(){
        return "matkul";
    }
}
