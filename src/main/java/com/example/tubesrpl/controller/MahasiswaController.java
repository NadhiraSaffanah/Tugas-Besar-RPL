package com.example.tubesrpl.controller; // Pastikan package sesuai

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.tubesrpl.model.Kelompok;
import com.example.tubesrpl.model.Penilaian;
import com.example.tubesrpl.model.TahapTubes; // GANTI Phase jadi TahapTubes
import com.example.tubesrpl.model.Tubes;
import com.example.tubesrpl.model.User;
import com.example.tubesrpl.repository.KelompokRepository;
import com.example.tubesrpl.repository.MatkulRepository;
import com.example.tubesrpl.repository.PenilaianRepository;
import com.example.tubesrpl.repository.TahapRepository; // GANTI PhaseRepository jadi TahapRepository
import com.example.tubesrpl.repository.TubesRepository;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@SessionAttributes("user")
@RequestMapping("/mahasiswa")
public class MahasiswaController {

    @Autowired
    private TubesRepository tubesRepository;

    @Autowired
    MatkulRepository matkulRepository;

    @Autowired
    private TahapRepository tahapRepository; // Rename

    @Autowired
    private KelompokRepository kelompokRepository;

    @Autowired
    private PenilaianRepository penilaianRepository;

    @ModelAttribute("user")
    public User userSession(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    @GetMapping("/profile-page")
    public String dosenProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";
        model.addAttribute("mahasiswa", user);

        return "profile-page";
    }

    @GetMapping("/home")
    public String homeMhsView(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";
        // Sementara hardcode semester 1
        Long semesterId = 1L;

        List<Tubes> tubes = tubesRepository.findAllBySemester(semesterId);

        // Sekarang pakai TahapRepository
        List<TahapTubes> phases = tahapRepository.findAllBySemester(semesterId);

        model.addAttribute("tubeslist", tubes);
        model.addAttribute("phaselist", phases);
        model.addAttribute("user", user);

        return "Mahasiswa/homeMhs";
    }

    @GetMapping("/course-details")
    public String courseDetailsMhs(@RequestParam(name = "id") Long matkulId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        boolean isLocked = false;

        // BARU, kalau bukan dosen redirect ke login
        if (user == null || !"mahasiswa".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

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

        if (tubesOpt.isPresent()) {
            Tubes tubes = tubesOpt.get();
            model.addAttribute("tubes", tubes);

            isLocked = tubes.isLocked();

            List<TahapTubes> listTahap = tahapRepository.findAllByTubesId(tubes.getId());
            model.addAttribute("listTahap", listTahap);
        } else {
            model.addAttribute("tubes", null);
            model.addAttribute("listTahap", null);
        }

        model.addAttribute("selectedMatkulId", matkulId);
        model.addAttribute("user", user);
        model.addAttribute("isGroupLocked", isLocked);

        return "Mahasiswa/course-details";
    }

    @GetMapping("/grading-phase")
    public String gradingPhase(@RequestParam(name = "id") Long matkulId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"mahasiswa".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

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

        Optional<Tubes> tubesOpt = tubesRepository.findById(matkulId);
        if (tubesOpt.isEmpty()) {
            model.addAttribute("listTahap", List.of());
            return "Mahasiswa/grading-phase";
        }

        Tubes tubes = tubesOpt.get();

        // ambil semua tahap
        List<TahapTubes> listTahap = tahapRepository.findAllByTubesId(tubes.getId());

        // === ISI NILAI PER TAHAP UNTUK USER LOGIN ===
        for (TahapTubes tahap : listTahap) {

            Penilaian penilaian = penilaianRepository.findNilaiByUserIdAndTahapId(
                    user.getId(), tahap.getId());

            if (penilaian != null && penilaian.getNilai() != null) {
                tahap.setNilaiTahap(penilaian.getNilai().intValue());
                tahap.setStatusPenilaian("Graded");
            } else {
                tahap.setNilaiTahap(null);
                tahap.setStatusPenilaian("Not Graded");
            }
        }

        model.addAttribute("listTahap", listTahap);
        model.addAttribute("tubes", tubes);
        model.addAttribute("selectedMatkulId", tubes.getIdMatkul());
        model.addAttribute("user", user);

        return "Mahasiswa/course-nav-grading-phase";
    }

    // ROUTING UNTUK COURSE PARTICIPANT
    @GetMapping("/course-nav-participant")
    public String courseNavParticipant(@RequestParam(name = "id", required = false) Long tubesId, Model model,
            HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";
        model.addAttribute("user", user);

        // ambil id matkul dari Tubes, ini buat back aja sih
        Long matkulId = null;
        Optional<Tubes> tubesOpt = tubesRepository.findById(tubesId);

        if (tubesOpt.isPresent()) {
            Tubes tubes = tubesOpt.get();
            matkulId = tubesOpt.get().getIdMatkul();

            model.addAttribute("matkulId", matkulId);
            model.addAttribute("selectedMatkulId", matkulId);
        }

        // ambil list participant dari db
        if (matkulId != null) {
            List<User> participants = matkulRepository.findParticipantsByMatkulId(matkulId);
            model.addAttribute("participantList", participants);
        }

        model.addAttribute("tubesId", tubesId);
        return "Mahasiswa/course-participant";
    }

    @GetMapping("/course-nav-group")
    public String showGroup(@RequestParam(name = "id") Long tubesId, Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";
        model.addAttribute("user", user);

        Optional<Tubes> tubesOpt = tubesRepository.findById(tubesId);
        if (tubesOpt.isPresent()) {
            Tubes tubes = tubesOpt.get();
            model.addAttribute("tubes", tubes);
            model.addAttribute("selectedMatkulId", tubes.getIdMatkul());

            tubesRepository.syncGroupCount(tubesId, tubes.getJmlKelompok());
        }

        // ambil data kelompok
        List<Kelompok> listKelompok = kelompokRepository.findAllByTubesId(tubesId);
        Kelompok myKelompok = kelompokRepository.findKelompokByUserAndTubes(user.getId(), tubesId);

        // untuk kasih tau status udah join kelompok atau belum
        boolean alreadyJoined = (myKelompok != null);

        for (Kelompok k : listKelompok) {
            List<User> anggotas = kelompokRepository.findAnggotaByKelompokId(k.getId());
            k.setListAnggota(anggotas);
        }

        model.addAttribute("listKelompok", listKelompok);
        model.addAttribute("user", user);
        model.addAttribute("tubesId", tubesId);
        model.addAttribute("alreadyJoined", alreadyJoined);
        model.addAttribute("myKelompok", myKelompok);

        return "Mahasiswa/course-nav-group";
    }

    @PostMapping("/course-nav-group/submit")
    public String pickGroup(@RequestParam(value = "id", required = false) Long matkulId,
            @RequestParam(value = "selectedGroup", required = false) Long groupId,
            @RequestParam("action") String action,
            Model model, HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        Long currentUserId = user.getId();

        if (matkulId == null) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Sistem tidak dapat mengidentifikasi Mata Kuliah yang diakses. Mohon akses ulang halaman.");
            return "redirect:/mahasiswa/course";
        }

        String redirectUrl = "redirect:/mahasiswa/course-nav-group?id=" + matkulId;

        try {
            boolean alreadyJoined = kelompokRepository.isUserAlreadyInAnyGroup(currentUserId, matkulId);

            if ("save".equalsIgnoreCase(action)) {
                if (alreadyJoined) {
                    redirectAttributes.addFlashAttribute("errorMessage", "You are already in another group");
                    return redirectUrl;
                }
                if (groupId == null) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Please choose a group");
                    return redirectUrl;
                }

                int maxCapacity = kelompokRepository.getMaxCapacityByGroupId(groupId);
                int currentMembers = kelompokRepository.getCurrentMembersByGroupId(groupId);

                if (currentMembers >= maxCapacity) {
                    redirectAttributes.addFlashAttribute("errorMessage", "This group is already full");
                    return redirectUrl;
                }

                // Join group
                kelompokRepository.saveUserGroup(currentUserId, groupId);
                redirectAttributes.addFlashAttribute("successMessage", "Successfully joined!");

            } else if ("remove".equalsIgnoreCase(action)) {
                // Remove group
                kelompokRepository.deleteUserGroup(currentUserId);
                redirectAttributes.addFlashAttribute("successMessage", "Successfully left group");

            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Action not recognized");
                return redirectUrl;
            }

        } catch (Exception e) {
            e.printStackTrace(); // atau gunakan logger
            redirectAttributes.addFlashAttribute("errorMessage", "Terjadi kesalahan sistem: " + e.getMessage());
        }

        return redirectUrl; // pastikan selalu redirect ke page yang sama
    }

    @PostMapping("/course-nav-group")
    public String handleGroupChoice(@RequestParam Long tubesId,
            @RequestParam(required = false) Long selectedGroup,
            @RequestParam String action,
            HttpSession session) {
        User user = (User) session.getAttribute("user");

        if ("save".equals(action)) {
        } else if ("remove".equals(action)) {
        }

        return "redirect:/mahasiswa/course-nav-group?id=" + tubesId;
    }

    @GetMapping("/course-nav-grading-phase")
    public String showGradingPhase(@RequestParam(name = "id") Long tubesId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null || !"dosen".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        Optional<Tubes> tubesOpt = tubesRepository.findById(tubesId);
        if (tubesOpt.isPresent()) {
            Tubes tubes = tubesOpt.get();
            model.addAttribute("matkulId", tubes.getIdMatkul()); // Simpan matkulId ke model supaya bisa dipakai di
                                                                 // tombol "Back"
        }

        List<TahapTubes> listTahap = tahapRepository.findAllByTubesId(tubesId);

        model.addAttribute("listTahap", listTahap);
        model.addAttribute("tubesId", tubesId);

        return "mahasiswa/course-nav-grading";
    }

    @GetMapping("/course/tahap/comment")
    @ResponseBody
    public ResponseEntity<String> getComment(
            @RequestParam("tahapId") Long tahapId, HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }

        Penilaian penilaian = penilaianRepository.findNilaiByUserIdAndTahapId(user.getId(), tahapId);
        String komentar = (penilaian != null && penilaian.getKomentar() != null)
                ? penilaian.getKomentar()
                : "No comment available.";

        return ResponseEntity.ok(komentar);
    }

}
