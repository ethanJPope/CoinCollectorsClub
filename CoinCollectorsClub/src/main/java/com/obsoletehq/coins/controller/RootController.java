package com.obsoletehq.coins.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
    @GetMapping("/")
    public String root(Authentication auth) {
        if (auth != null && auth.isAuthenticated()) {
            return "redirect:/game";
        }
        return "redirect:/login";
    }
}
