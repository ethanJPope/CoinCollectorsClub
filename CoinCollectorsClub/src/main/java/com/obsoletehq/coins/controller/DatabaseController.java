package com.obsoletehq.coins.controller;

import com.obsoletehq.coins.model.Coin;
import com.obsoletehq.coins.model.User;
import com.obsoletehq.coins.model.UserCoinDTO;
import com.obsoletehq.coins.repository.CoinRepository;
import com.obsoletehq.coins.repository.UserRepository;
import com.obsoletehq.coins.repository.CoinFamilyRepository;
import com.obsoletehq.coins.service.CoinService;
import com.obsoletehq.coins.service.UserService;
import com.obsoletehq.coins.model.CoinFamily;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class DatabaseController {

    @Autowired
    private CoinService coinService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinRepository coinRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CoinFamilyRepository coinFamilyRepository;

    @GetMapping("/database")
    public String database(Model model, Authentication authentication) {
        List<Coin> coins = coinService.getAllCoins();
        List<User> users = userService.getAllUsers();

        // Update collection worth for all users before displaying
        for (User user : users) {
            userService.updateUserCollectionWorth(user);
            userRepo.save(user);
        }

        // Create DTOs with resolved coin objects
        List<UserCoinDTO> userDTOs = new ArrayList<>();
        for (User user : users) {
            UserCoinDTO dto = new UserCoinDTO(user);
            Map<UUID, Integer> coinCollection = user.getCoinCollection();
            if (coinCollection != null) {
                for (Map.Entry<UUID, Integer> entry : coinCollection.entrySet()) {
                    coinRepo.findById(entry.getKey()).ifPresent(coin ->
                            dto.addCoinDetail(coin, entry.getValue())
                    );
                }
            }

            // Calculate completed families
            List<CoinFamily> allFamilies = coinFamilyRepository.findAll();
            for (CoinFamily family : allFamilies) {
                List<Coin> coinsInFamily = coinRepo.findByCoinFamily(family);
                boolean hasAllCoins = true;

                for (Coin coin : coinsInFamily) {
                    if (coinCollection == null || !coinCollection.containsKey(coin.getId()) || coinCollection.get(coin.getId()) == 0) {
                        hasAllCoins = false;
                        break;
                    }
                }

                if (hasAllCoins && !coinsInFamily.isEmpty()) {
                    dto.addCompletedFamily(family.getDisplayName());
                }
            }

            // Set calculated worth from user entity
            dto.setCalculatedWorth(user.getCollectionWorth() != null ? user.getCollectionWorth() : BigDecimal.ZERO);

            userDTOs.add(dto);
        }

        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
            // Check if user is admin - adjust this logic based on your role implementation
            model.addAttribute("isAdmin", authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")));
        }

        model.addAttribute("coins", coins);
        model.addAttribute("users", userDTOs);
        model.addAttribute("coinFamilies", coinFamilyRepository.findAll());
        return "database";
    }

    @PostMapping("/database/add-coin-to-user")
    public String addCoinToUser(@RequestParam String username, @RequestParam UUID coinId, Model model) {
        try {
            userService.addCoinToUserByUsername(username, coinId);
            model.addAttribute("successMessage", "Coin successfully added to user: " + username);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error adding coin to user: " + e.getMessage());
        }

        return "redirect:/database";
    }

    @GetMapping("/database/console")
    public String redirectToH2Console() {
        return "redirect:/h2-console";
    }

    @GetMapping("/rest-databases")
    public String restDatabases() {
        coinRepo.deleteAll();
        userRepo.deleteAll();
        coinService.initializeCoins();
        userService.initializeTestUsers();
        return "redirect:/database";
    }
}
