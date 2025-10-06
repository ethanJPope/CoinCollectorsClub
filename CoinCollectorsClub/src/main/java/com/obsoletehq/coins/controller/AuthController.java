package com.obsoletehq.coins.controller;

import com.obsoletehq.coins.model.User;
import com.obsoletehq.coins.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error,
                       @RequestParam(required = false) String logout,
                       Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully");
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        return "register";
    }
    @PostMapping("/register")
    public String handleRegistration(@RequestParam String username,
                                     @RequestParam String password,
                                     @RequestParam String email,
                                     Model model) {
        try {
            // Use registerUser method which handles password encryption
            userService.registerUser(username, email, password);

            logger.info("New user registered: {}", username);
            model.addAttribute("message", "Registration successful! Please login.");
            return "login";

        } catch (RuntimeException e) {
            logger.error("Registration failed for user: {}", username, e);
            model.addAttribute("error", e.getMessage());
            return "register";
        } catch (Exception e) {
            logger.error("Registration failed for user: {}", username, e);
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }
    @PostMapping("/login")
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              HttpSession session,
                              Model model) {
        try {
            User user = userService.authenticate(username, password);

            if (user != null) {
                session.setAttribute("user", user);
                session.setAttribute("username", user.getUsername());
                session.setAttribute("role", user.getRole());
                session.setAttribute("isAdmin", "ROLE_ADMIN".equals(user.getRole()));

                logger.info("User {} logged in with role: {}", username, user.getRole());
                return "redirect:/game";
            } else {
                model.addAttribute("error", "Invalid username or password");
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Login failed: " + e.getMessage());
            return "login";
        }
    }


}
