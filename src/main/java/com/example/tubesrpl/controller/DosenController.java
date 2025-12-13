package com.example.tubesrpl.controller;

import com.example.tubesrpl.model.Kelompok;
import com.example.tubesrpl.model.Matkul;
import com.example.tubesrpl.model.TahapTubes;
import com.example.tubesrpl.model.Tubes;
import com.example.tubesrpl.model.User;
import com.example.tubesrpl.repository.KelompokRepository;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class DosenController {
    
    // @Autowired
    // private UserRepository userRepository; // belum kepake

    @Autowired
    private MatkulRepository matkulRepository;

    @Autowired
    private TubesRepository tubesRepository;

    @Autowired
    private TahapRepository tahapRepository;

    @Autowired
    private KelompokRepository kelompokRepository;

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

        // ambil data matkul milik Dosen tersebut
        List<Matkul> courses = matkulRepository.findAllByUserId(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("listCourses", courses);

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
        model.addAttribute("user", user);

        return "dosen/course-details";
    }


    // ROUTING UNTUK COURSE NAVBAR 
    // ROUTING UNTUK COURSE GRADING
    @GetMapping("/dosen/course/nav/grading")
    public String courseNavGrading(@RequestParam(name = "id") Long tubesId, HttpSession session, Model model) {
        
        User user = (User) session.getAttribute("user");
        
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user); 

        Optional<Tubes> tubesOpt = tubesRepository.findById(tubesId);
        if (tubesOpt.isPresent()) {
            Tubes tubes = tubesOpt.get();
            model.addAttribute("matkulId", tubes.getIdMatkul()); // Simpan matkulId ke model supaya bisa dipakai di tombol "Back"
        }

        List<TahapTubes> listTahap = tahapRepository.findAllByTubesId(tubesId);
        
        model.addAttribute("listTahap", listTahap);
        model.addAttribute("tubesId", tubesId);
        
        return "dosen/course-nav-grading";
    }

    // API UNTUK CREATE PHASE (AJAX)
    @PostMapping("/dosen/course/grading/phase/create-api")
    @ResponseBody
    public ResponseEntity<String> createPhaseApi(
            @RequestParam Long tubesId,
            @RequestParam String namaTahap,
            @RequestParam String deskripsi,
            @RequestParam String rubrik,
            @RequestParam String tanggalAkhir) { 
        try {
            LocalDate date = LocalDate.parse(tanggalAkhir);
            
            tahapRepository.createTahap(namaTahap, deskripsi, rubrik, date, tubesId);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/dosen/course/nav/grading/add")
    public String courseNavGradingAdd() {
        return "dosen/course-nav-grading-add";
    }

    @GetMapping("/dosen/course/nav/grading/phase")
    public String gradingPhase() {
        return "dosen/course-nav-grading-phase";
    }

    // update fase
    @PostMapping("/dosen/course/grading/phase/update-api")
    @ResponseBody
    public ResponseEntity<String> updatePhaseApi(@RequestParam Long id,
                                            @RequestParam String namaTahap,
                                            @RequestParam String deskripsi,
                                            @RequestParam String rubrik,
                                            @RequestParam LocalDate tanggalAkhir) {
        try {
            tahapRepository.updateTahap(id, namaTahap, deskripsi, rubrik, tanggalAkhir);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    // ROUTING UNTUK COURSE PARTICIPANT
    @GetMapping("/dosen/course/nav/participant")
    public String courseNavParticipant(@RequestParam(name = "id") Long tubesId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        
        // ambil id matkul dari Tubes, ini buat back aja sih
        Long matkulId = null;
        Optional<Tubes> tubesOpt = tubesRepository.findById(tubesId);
        if (tubesOpt.isPresent()) {
            matkulId = tubesOpt.get().getIdMatkul();
            model.addAttribute("matkulId", matkulId);
        }

        // ambil list participant dari db
        if (matkulId != null) {
            List<User> participants = matkulRepository.findParticipantsByMatkulId(matkulId);
            model.addAttribute("participantList", participants);
        }

        model.addAttribute("tubesId", tubesId);
        return "dosen/course-nav-participant";
    }

    // ROUTING UNTUK COURSE GROUP
    @GetMapping("/dosen/course/nav/group")
    public String courseNavGroup(@RequestParam(name = "id") Long tubesId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        Optional<Tubes> tubesOpt = tubesRepository.findById(tubesId);
        if (tubesOpt.isPresent()) {
            Tubes tubes = tubesOpt.get();
            model.addAttribute("tubes", tubes);
            model.addAttribute("matkulId", tubes.getIdMatkul());
            
            tubesRepository.syncGroupCount(tubesId, tubes.getJmlKelompok());
        }

        List<Kelompok> listKelompok = kelompokRepository.findAllByTubesId(tubesId);
        
        for (Kelompok k : listKelompok) {
            List<User> anggotas = kelompokRepository.findAnggotaByKelompokId(k.getId());
            k.setListAnggota(anggotas);
        }

        model.addAttribute("listKelompok", listKelompok); 
        model.addAttribute("user", user);
        model.addAttribute("tubesId", tubesId);

        return "dosen/course-nav-group-view"; 
    }

    // --> BARU DITAMBAHIN
    @GetMapping("/dosen/course/nav/group/edit")
    public String courseNavGroupEdit(@RequestParam Long groupId, 
                                     @RequestParam Long tubesId, 
                                     Model model, 
                                     HttpSession session) {
        
        User user = (User) session.getAttribute("user");
        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        Kelompok kelompok = kelompokRepository.findById(groupId);
        List<User> currentMembers = kelompokRepository.findAnggotaByKelompokId(groupId); 
        kelompok.setListAnggota(currentMembers);

        Optional<Tubes> tubesOpt = tubesRepository.findById(tubesId);
        Long matkulId = tubesOpt.isPresent() ? tubesOpt.get().getIdMatkul() : null;

        List<User> availableStudents = kelompokRepository.findAvailableStudents(matkulId, tubesId);

        model.addAttribute("kelompok", kelompok);
        model.addAttribute("availableStudents", availableStudents);
        model.addAttribute("tubesId", tubesId);
        model.addAttribute("user", user);

        return "dosen/course-nav-group-edit";
    }

    @PostMapping("/dosen/course/update-api")
    @ResponseBody
    public ResponseEntity<String> updateCourseApi(@RequestParam Long id, // ID Tubes
                                                  @RequestParam String namaTubes,
                                                  @RequestParam String deskripsi,
                                                  @RequestParam int jmlKelompok) { 
        try {
            tubesRepository.updateTubes(id, namaTubes, deskripsi, jmlKelompok);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    // API untuk Proses Simpan Edit (Update)
    @PostMapping("/dosen/course/nav/group/update-api") 
    @ResponseBody
    public ResponseEntity<String> updateGroupApi(
            @RequestParam Long groupId,
            @RequestParam String namaKelompok,
            @RequestParam int jmlAnggota,
            @RequestParam(required = false) List<Long> memberIds 
    ) {
        try {
            kelompokRepository.updateKelompok(groupId, namaKelompok, jmlAnggota);
            kelompokRepository.updateAnggotaKelompok(groupId, memberIds);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
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

    // Endpoint API untuk Toggle Lock 
    @PostMapping("/dosen/course/nav/group/toggle-lock")
    @ResponseBody
    public ResponseEntity<String> toggleLock(@RequestParam Long tubesId, @RequestParam boolean lockStatus) {
        try {
            tubesRepository.updateLockStatus(tubesId, lockStatus);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }
}
