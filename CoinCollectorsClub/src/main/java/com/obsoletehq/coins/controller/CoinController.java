package com.obsoletehq.coins.controller;

import com.obsoletehq.coins.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CoinController {

    private final UserRepository playerRepo;
    private static final Logger logger = LoggerFactory.getLogger(CoinController.class);

    @Autowired
    public CoinController(UserRepository playerRepo) {
        this.playerRepo = playerRepo;
    }

}
