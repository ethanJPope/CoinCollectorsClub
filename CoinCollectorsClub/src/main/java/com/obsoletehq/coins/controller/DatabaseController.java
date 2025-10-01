package com.obsoletehq.coins.controller;

import com.obsoletehq.coins.model.Coin;
import com.obsoletehq.coins.model.CoinFamily;
import com.obsoletehq.coins.model.User;
import com.obsoletehq.coins.repository.CoinFamilyRepo;
import com.obsoletehq.coins.repository.CoinRepo;
import com.obsoletehq.coins.repository.UserRepo;
import com.obsoletehq.coins.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DatabaseController {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseController.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CoinRepo coinRepo;

    @Autowired
    private CoinFamilyRepo coinFamilyRepo;

    @Autowired
    private UserService userService;

    @GetMapping("/database")
    public String showDatabase(Model model) {
        try {
            model.addAttribute("users", userRepo.findAll());
            model.addAttribute("coins", coinRepo.findAll());
            model.addAttribute("families", coinFamilyRepo.findAll());

            logger.info("Database view loaded successfully");
            return "database";
        } catch (Exception e) {
            logger.error("Error in showDatabase: ", e);
            throw e;
        }
    }

    @GetMapping("/database/reset")
    public String resetDatabase() {
        try {
            userRepo.deleteAll();
            coinRepo.deleteAll();
            coinFamilyRepo.deleteAll();
            logger.info("Database cleared successfully");
            return "redirect:/database";
        } catch (Exception e) {
            logger.error("Error clearing database: ", e);
            throw e;
        }
    }


}
