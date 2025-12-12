package com.example.tubesrpl.controller;

import com.example.tubesrpl.model.Matkul;
import com.example.tubesrpl.model.TahapTubes;
import com.example.tubesrpl.model.Tubes;
import com.example.tubesrpl.model.User;
import com.example.tubesrpl.repository.MatkulRepository; 
import com.example.tubesrpl.repository.TahapRepository;
import com.example.tubesrpl.repository.TubesRepository;
// import com.example.tubesrpl.repository.UserRepository; // belum kepake

import jakarta.servlet.http.HttpSession;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class DosenController {
    
    // @Autowired
    // private UserRepository userRepository; // belum kepake

    @Autowired
    private MatkulRepository matkulRepository;

    @Autowired
    private TubesRepository tubesRepository; // BARU

    @Autowired
    private TahapRepository tahapRepository; // BARU

    @GetMapping("/dosen/home")
    public String dosenHome(HttpSession session, Model model) { 
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user); //ini diganti user biar sama kaya templatenya
        
        List<Tubes> tubes = tubesRepository.findAllByUserId((long) user.getId()); //NEW buat list ongoing
        model.addAttribute("tubeslist", tubes); //sementara ambil semester 1 doang

        return "Dosen/homedosen";
    }

    @GetMapping("/dosen/profile")
    public String dosenProfile(HttpSession session, Model model) { 
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("dosen", user);
        
        return "dosen/profile-page"; 
    }

    
    // ROUTING UNTUK COURSE (BARU DITAMBAHIN BACKEND)
    @GetMapping("/dosen/course")
    public String courseHome(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        // kalo belum login, redirect balik ke halaman login
        if (user == null) {
            return "redirect:/login";
        }

        // Ambil Data Matkul milik Dosen tersebut
        List<Matkul> courses = matkulRepository.findAllByUserId(user.getId());

        model.addAttribute("dosen", user);      // Di HTML dipanggil ${dosen.nama}
        model.addAttribute("listCourses", courses); // Di HTML dipanggil ${listCourses}

        return "dosen/course-home";
    }

    // BARU
    @GetMapping("/dosen/course/details")
    public String courseDetails(@RequestParam(name = "id") Long matkulId, HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");

        // BARU, kalau bukan dosen redirect ke login
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

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
        Optional<Tubes> tubesOpt = tubesRepository.findByMatkulId(matkulId);

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
        model.addAttribute("dosen", user);

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

    @GetMapping("/dosen/course/nav/group/edit")
    public String courseNavGroupEdit() {
        return "dosen/course-nav-group-edit";
    }


    // Endpoint API untuk handle update (pake AJAX)
    @PostMapping("/dosen/course/update-api")
    @ResponseBody // biar spring ga nyari file HTML, tapi return data
    public ResponseEntity<String> updateCourseApi(@RequestParam Long id,
                                                  @RequestParam String namaTubes,
                                                  @RequestParam String deskripsi,
                                                  @RequestParam int jmlKelompok) {
        try {
            // Panggil service/repository untuk update
            tubesRepository.updateTubes(id, namaTubes, deskripsi, jmlKelompok);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    // Endpoint API untuk bikin Tubes Baru
    @PostMapping("/dosen/course/create-api")
    @ResponseBody
    public ResponseEntity<String> createCourseApi(@RequestParam Long matkulId,
                                                  @RequestParam String namaTubes,
                                                  @RequestParam String deskripsi,
                                                  @RequestParam int jmlKelompok) { 
        try {
            tubesRepository.createTubes(namaTubes, deskripsi, jmlKelompok, matkulId);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }
}
