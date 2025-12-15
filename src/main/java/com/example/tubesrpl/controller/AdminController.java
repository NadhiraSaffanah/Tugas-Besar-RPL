package com.example.tubesrpl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.tubesrpl.model.Matkul;
import com.example.tubesrpl.model.Semester;
import com.example.tubesrpl.model.User;
import com.example.tubesrpl.repository.MatkulRepository;
import com.example.tubesrpl.repository.SemesterRepository;
import com.example.tubesrpl.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SessionAttributes("user")
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    SemesterRepository semesterRepository;

    @Autowired
    MatkulRepository matkulRepository;

    @Autowired
    UserRepository userRepository;

    @ModelAttribute("user") //supaya ga nerima parameter HttpSesison berkali kali
    public User userSession(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user; 
    }
    
    @GetMapping("/home")
    public String homeAdminView(@ModelAttribute("user") User user){
        //role base restriction
        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/login";
        }
        return "Admin/homeAdmin";
    }

    @GetMapping("/semesters")
    public String semestersAdminView(@ModelAttribute("user") User user, Model model){
        //role base restriction
        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/login";
        }
        List<Semester> semesterList = semesterRepository.findAll();
        model.addAttribute("semesterList", semesterList); //matkul list karena menggunakan template yang sama dengan matkul
        return "Admin/semester";
    }

    @GetMapping("/semesters/{idSemester}/courses")
    public String coursesAdminView(@ModelAttribute("user") User user, @PathVariable Long idSemester, Model model){
        //role base restriction
        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/login";
        }
        List<Matkul> matkulList = matkulRepository.findAllBySemester(idSemester);
        Semester semester = semesterRepository.findById(idSemester);
        model.addAttribute("matkulList", matkulList);
        model.addAttribute("idSemester", idSemester);
        model.addAttribute("semester", semester);
        return "Admin/matkul";
    }

    @PostMapping("/courses/create")
    public String createCourses(@RequestParam("idSemester") Long idSemester,
                                @RequestParam("courses") String[] courses) {
        // Parse courses: format "namaMatkul,kelasMatkul"
        for (String course : courses) {
            String[] parts = course.split(",");
            if (parts.length == 2) {
                String namaMatkul = parts[0].trim();
                String kelasMatkul = parts[1].trim();
                
                if (!namaMatkul.isEmpty() && !kelasMatkul.isEmpty()) {
                    Long matkulId = matkulRepository.createMatkul(namaMatkul, kelasMatkul);
                    matkulRepository.linkMatkulToSemester(matkulId, idSemester);
                }
            }
        }
        
        return "redirect:/admin/semesters/" + idSemester + "/courses";
    }

    @PostMapping("/courses/{idMatkul}/delete")
    public String deleteMatkul(@PathVariable Long idMatkul, @RequestParam("idSemester") Long idSemester) {
        try {
            matkulRepository.deleteMatkul(idMatkul);
            return "redirect:/admin/semesters/" + idSemester + "/courses";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/semesters/" + idSemester + "/courses";
        }
    }

    @PostMapping("/semesters/create")
    public String createSemester(@RequestParam("startDate") String startDateStr,
                                 @RequestParam("endDate") String endDateStr,
                                 @RequestParam("jenis") String jenis) {
        try {
            java.time.LocalDate startDate = java.time.LocalDate.parse(startDateStr);
            java.time.LocalDate endDate = java.time.LocalDate.parse(endDateStr);
            semesterRepository.createSemester(startDate, endDate, jenis);
            return "redirect:/admin/semesters";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/semesters?error=true";
        }
    }

    @PostMapping("/semesters/{idSemester}/delete")
    public String deleteSemester(@PathVariable Long idSemester) {
        try {
            semesterRepository.deleteSemester(idSemester);
            return "redirect:/admin/semesters";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/semesters?error=true"; //ini blum tau mau diapain kalo error
        }
    }

    // Manage participants page
    @GetMapping("/semesters/{idSemester}/courses/{idMatkul}/participants")
    public String manageParticipantsView(@ModelAttribute("user") User user, @PathVariable Long idSemester, 
                                  @PathVariable Long idMatkul, 
                                  Model model) {
        List<User> allParticipants = userRepository.findUsersByMatkulId(idMatkul);
        // Filter by role
        List<User> dosenList = allParticipants.stream().filter(u -> "dosen".equals(u.getRole())).toList();
        List<User> mahasiswaList = allParticipants.stream().filter(u -> "mahasiswa".equals(u.getRole())).toList();
        
        Matkul matkul = matkulRepository.findById(idMatkul);
        Semester semester = semesterRepository.findById(idSemester);
        
        model.addAttribute("dosenList", dosenList);
        model.addAttribute("mahasiswaList", mahasiswaList);
        model.addAttribute("idMatkul", idMatkul);
        model.addAttribute("idSemester", idSemester);
        model.addAttribute("matkul", matkul);
        model.addAttribute("semester", semester);
        return "Admin/manageParticipants";
    }

    // Search users API (AJAX)
    @GetMapping("/users/search")
    @ResponseBody
    public ResponseEntity<List<User>> searchUsers(@ModelAttribute("user") User user, @RequestParam String role, @RequestParam String searchTerm) {
        try {
            List<User> users = userRepository.searchUsers(role, searchTerm);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // Add participants (link users to matkul)
    @PostMapping("/semesters/{idSemester}/courses/{idMatkul}/participants/add")
    public String addParticipants(@ModelAttribute("user") User user,
                                 @PathVariable Long idSemester,
                                 @PathVariable Long idMatkul,
                                 @RequestParam("userIds") Long[] userIds) {
        try {
            for (Long userId : userIds) {
                if (userId != null) {
                    userRepository.linkUserToMatkul(userId, idMatkul);
                }
            }
            return "redirect:/admin/semesters/" + idSemester + "/courses/" + idMatkul + "/participants";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/semesters/" + idSemester + "/courses/" + idMatkul + "/participants?error=true";
        }
    }

    // Remove participant (unlink user from matkul)
    @PostMapping("/semesters/{idSemester}/courses/{idMatkul}/participants/{userId}/remove")
    public String removeParticipant(@PathVariable Long idSemester,
                                   @PathVariable Long idMatkul,
                                   @PathVariable Long userId) {
        try {
            userRepository.unlinkUserFromMatkul(userId, idMatkul);
            return "redirect:/admin/semesters/" + idSemester + "/courses/" + idMatkul + "/participants";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/semesters/" + idSemester + "/courses/" + idMatkul + "/participants?error=true";
        }
    }
}
