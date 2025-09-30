package com.obsoletehq.coins.controller;

import com.obsoletehq.coins.model.Player;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class CoinController {

    @GetMapping("/leaderboard")
    public String leaderboard(Model model) {
        List<Player> players = List.of(
                new Player("Alice", 55.25),
                new Player("Bob", 48.90),
                new Player("Charlie", 12.40)
        );

        model.addAttribute("players", players);
        return "leaderboard";
    }
}
