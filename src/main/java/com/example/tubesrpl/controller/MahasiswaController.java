package com.example.tubesrpl.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.tubesrpl.model.User;
import com.example.tubesrpl.model.Matkul;
import com.example.tubesrpl.model.TahapTubes;
import com.example.tubesrpl.model.Tubes;
import com.example.tubesrpl.repository.MatkulRepository;
import com.example.tubesrpl.repository.TahapRepository;
import com.example.tubesrpl.repository.TubesRepository;
import jakarta.servlet.http.HttpSession;
import com.example.tubesrpl.model.Group;
import com.example.tubesrpl.repository.GroupRepository;

@Controller
public class MahasiswaController {
    @Autowired
    private MatkulRepository matkulRepository;

    @Autowired
    private TubesRepository tubesRepository; // BARU

    @Autowired
    private TahapRepository tahapRepository; // BARU

    @Autowired
    private GroupRepository groupRepository;//baru 

    @GetMapping("/mahasiswa/home")
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

        return "profile-page";
    }
    
    // BARU
    @GetMapping("/mhs/course/details")
    public String courseDetails(@RequestParam(name = "id") Long matkulId, HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

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

        boolean isLocked = false;
        String groupName = null;

        if (tubesOpt.isPresent()) {
            Tubes tubes = tubesOpt.get();
            model.addAttribute("tubes", tubes);

            //ambil status kelompok (locked/unlocked) - baru 
            String statusKelompok = tubes.getStatusKelompok();
        
            // Cek jika statusnya adalah 'Locked' (mengabaikan case)
            if (statusKelompok != null && "Locked".equalsIgnoreCase(statusKelompok)) {
                isLocked = true;
            }
            List<TahapTubes> listTahap = tahapRepository.findAllByTubesId(tubes.getId());

            //ambil kelompok berdasarkan tubes dan id user 
            Optional<Group> groupOpt = groupRepository.findKelompokDetailByUserIdAndTubesId(
                user.getId(), 
                tubes.getId() // Gunakan ID Tubes yang baru saja diambil
            );

            if (groupOpt.isPresent()) {
                Group group = groupOpt.get();
                model.addAttribute("group", group); // Kirim objek group ke Model (opsional)
                groupName = group.getNama_kelompok(); // Ambil nama kelompok
            }
            model.addAttribute("listTahap", listTahap);
        } else {
            model.addAttribute("tubes", null);
            model.addAttribute("listTahap", null);
        }

        model.addAttribute("selectedMatkulId", matkulId);
        model.addAttribute("mahasiswa", user);
        model.addAttribute("isGroupLocked", isLocked);
        model.addAttribute("groupName", groupName);

        return "mahasiswa/course-details";
    }
    

    // ROUTING GRADING PHASE   --> BARU DITAMBAHIN
    @GetMapping("/mahasiswa/course/nav/grading/phase")
    public String gradingPhase() {
        return "mahasiswa/course-nav-grading-phase";
    }

    @GetMapping("/mahasiswa/course/nav/group")
    public String Group() {
        return "mahasiswa/course-nav-group";
    }

}
