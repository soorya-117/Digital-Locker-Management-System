package com.digitallocker.controller;

import com.digitallocker.entity.User;
import com.digitallocker.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home(HttpSession session) {
        return session.getAttribute("loggedInUser") == null ? "redirect:/login" : "redirect:/dashboard";
    }

    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password,
                           Model model) {
        if (name.isBlank() || email.isBlank() || password.length() < 6) {
            model.addAttribute("error", "Enter all details. Password must contain at least 6 characters.");
            return "register";
        }

        String error = userService.register(name.trim(), email.trim().toLowerCase(), password);
        if (error != null) {
            model.addAttribute("error", error);
            return "register";
        }

        model.addAttribute("message", "Registration successful. Please log in.");
        return "login";
    }

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        User user = userService.login(email.trim().toLowerCase(), password);

        if (user == null) {
            model.addAttribute("error", "Invalid email or password.");
            return "login";
        }

        session.setAttribute("loggedInUser", user.getId());
        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
