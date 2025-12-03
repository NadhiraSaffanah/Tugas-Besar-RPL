package com.example.tubesrpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/forgot-password")
    public String forgotPassPage() {
        return "forgotPass";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email, @RequestParam String password, HttpSession session, Model model){
        // ngecek email nya ada di db ga
        Optional<User> userOpt = userRepository.findByEmail(email);

        if(userOpt.isPresent()){
            User user = userOpt.get();

            // Cek Password (ingat, ini masih plain text)
            if (user.getPassword().equals(password)) {
                
                session.setAttribute("user", user);

                // Redirect sesuai Role
                switch (user.getRole()) {
                    case "mahasiswa": return "redirect:/home-mahasiswa";
                    case "dosen": return "redirect:/home-dosen";
                    case "admin": return "redirect:/home-admin";
                    default:
                        model.addAttribute("error", "Role tidak ada");
                        return "login";
                }
            }
        }

        model.addAttribute("error", "Email atau Password salah!");
        return "login";
    }

    @GetMapping("/home-mahasiswa")
    public String homeMhs() { return "homeMhs"; }
    
    @GetMapping("/home-dosen")
    public String homeDosen() { return "homeDosen"; }
    
    @GetMapping("/home-admin")
    public String homeAdmin() { return "homeAdmin"; }
} 