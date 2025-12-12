package com.example.tubesrpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.tubesrpl.model.Phase;
import com.example.tubesrpl.model.Tubes;
import com.example.tubesrpl.repository.TubesRepository;

import lombok.extern.slf4j.Slf4j;

import com.example.tubesrpl.repository.PhaseRepository;

@Slf4j
@Controller
public class MahasiswaController {
    @Autowired 
    private TubesRepository tubesRepository;

    @Autowired
    private PhaseRepository phaseRepository;

    @GetMapping("/mahasiswa/home")
    public String homeMhsView(Model model){
        List<Tubes> tubes = tubesRepository.findAllBySemester((long) 1);
        List<Phase> phase = phaseRepository.findAllBySemester((long) 1);
       
        model.addAttribute("tubeslist", tubes); //sementara ambil semester 1 doang
        model.addAttribute("phaselist", phase);
        return "Mahasiswa/homeMhs";
    }
}