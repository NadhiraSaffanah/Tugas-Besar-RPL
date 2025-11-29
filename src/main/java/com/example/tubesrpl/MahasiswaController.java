package com.example.tubesrpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MahasiswaController {
    @Autowired 
    private TubesRepository tubesRepository;

    @GetMapping("/home-mahasiswa")
    public String homeMhsView(Model model){
        List<Tubes> tubes = tubesRepository.findAllBySemester((long) 1);
        model.addAttribute("tubeslist", tubes); //sementara ambil semester 1 doang
        return "Mahasiswa/homeMhs";
    }
}
