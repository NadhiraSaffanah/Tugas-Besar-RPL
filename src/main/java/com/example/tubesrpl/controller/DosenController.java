package com.example.tubesrpl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.tubesrpl.repository.UserRepository;

@Controller
public class DosenController {
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dosen/home")
    public String dosenHome() { 
        return "dosen/homeDosen"; 
    }

    @GetMapping("/dosen/profile")
    public String dosenProfile() { 
        return "dosen/profile-page"; 
    }

    
    // ROUTING UNTUK COURSE
    @GetMapping("/dosen/course")
    public String courseHome() {
        return "dosen/course-home";
    }

    @GetMapping("/dosen/course/details")
    public String courseDetails() {
        return "dosen/course-details";
    }



    // ROUTING UNTUK COURSE NAVBAR 
    // ROUTING UNTUK COURSE GRADING
    @GetMapping("/dosen/course/nav/grading")
    public String courseNavGrading() {
        return "dosen/course-nav-grading";
    }

    @GetMapping("/dosen/course/nav/grading/add")
    public String courseNavGradingAdd() {
        return "dosen/course-nav-grading-add";
    }


    // ROUTING GRADING PHASE   --> BARU DITAMBAHIN
    @GetMapping("/dosen/course/nav/grading/phase")
    public String gradingPhase() {
        return "dosen/course-nav-grading-phase";
    }

    // --> BARU DITAMBAHIN
    @GetMapping("/dosen/course/nav/grading/phase/details")
    public String gradingPhaseDetails() {
        return "dosen/course-nav-grading-phase-details";
    }

    // --> BARU DITAMBAHIN
    @GetMapping("/dosen/course/nav/grading/phase/details/edit")
    public String gradingPhaseDetailsEdit() {
        return "dosen/course-nav-grading-phase-details-edit";
    }



    // ROUTING UNTUK COURSE PARTICIPANT
    @GetMapping("/dosen/course/nav/participant")
    public String courseNavParticipant() {
        return "dosen/course-nav-participant";
    }


    // ROUTING UNTUK COURSE GROUP
    @GetMapping("/dosen/course/nav/group")
    public String courseNavGroup() {
        return "dosen/course-nav-group";
    }

    @GetMapping("/dosen/course/nav/group/view")
    public String courseNavGroupView() {
        return "dosen/course-nav-group-view";
    }

    @GetMapping("/dosen/course/nav/group/add")
    public String courseNavGroupAdd() {
        return "dosen/course-nav-group-add";
    }

    // --> BARU DITAMBAHIN
    @GetMapping("/dosen/course/nav/group/edit")
    public String courseNavGroupEdit() {
        return "dosen/course-nav-group-edit";
    }


}
