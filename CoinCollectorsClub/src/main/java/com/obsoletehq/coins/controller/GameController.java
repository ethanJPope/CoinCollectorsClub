package com.obsoletehq.coins.controller;

import com.obsoletehq.coins.service.CoinService;
import com.obsoletehq.coins.service.PackService;
import com.obsoletehq.coins.service.CollectionService;
import com.obsoletehq.coins.model.User;
import com.obsoletehq.coins.service.UserService;
import com.obsoletehq.coins.model.UserCoinDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.obsoletehq.coins.game.PackType;
import com.obsoletehq.coins.model.Coin;

import java.util.List;

@Controller
public class GameController {

    @Autowired
    private PackService packService;

    @Autowired
    private CollectionService collectionService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;

    @GetMapping("/game")
    public String showGame(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            return "redirect:/login";
        }

        // Add user role information to model
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        String username = authentication.getName();
        User user = userService.findByUsername(username); // or however you retrieve users
        model.addAttribute("user", user);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("username", authentication.getName());
        model.addAttribute("packTypes", PackType.values());

        return "game";
    }

    @GetMapping("/collection")
    public String showCollection(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        UserCoinDTO userCollection = coinService.getUserCollection(user);

        model.addAttribute("userCollection", userCollection);
        model.addAttribute("username", authentication.getName());

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        return "collection";
    }

//    @PostMapping("/game/buy-pack")
//    public String buyPack(@RequestParam String packType,
//                          Authentication authentication,
//                          RedirectAttributes redirectAttributes) {
//        try {
//            String username = authentication.getName();
//            User user = userService.findByUsername(username);
//
//            int packCost;
//            switch (packType) {
//                case "COMMON":
//                    packCost = 50;
//                    break;
//                case "PREMIUM":
//                    packCost = 100;
//                    break;
//                case "LEGENDARY":
//                    packCost = 200;
//                    break;
//                default:
//                    redirectAttributes.addFlashAttribute("error", "Invalid pack type");
//                    return "redirect:/game";
//            }
//
//            if (user.getTokens() < packCost) {
//                redirectAttributes.addFlashAttribute("error", "Not enough tokens!");
//                return "redirect:/game";
//            }
//
//            user.setTokens(user.getTokens() - packCost);
//
//            userService.updateUser(user);
//
//            redirectAttributes.addFlashAttribute("success", "Pack purchased successfully!");
//
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("error", "Error purchasing pack: " + e.getMessage());
//        }
//
//        return "redirect:/game";
//    }


    @PostMapping("game/buy-pack")
    public String openPack(@RequestParam PackType packType, RedirectAttributes redirectAttributes) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            User user = userService.findByUsername(username);
            int packCost;
            switch (packType) {
                case COMMON:
                    packCost = 50;
                    break;
                case PREMIUM:
                    packCost = 100;
                    break;
                case LEGENDARY:
                    packCost = 200;
                    break;
                default:
                    redirectAttributes.addFlashAttribute("error", "Invalid pack type");
                    return "redirect:/game";
            }
            if (user.getTokens() < packCost) {
                redirectAttributes.addFlashAttribute("error", "Not enough tokens!");
                return "redirect:/game";
            }
            user.setTokens(user.getTokens() - packCost);
            userService.updateUser(user);

            if ("anonymousUser".equals(username)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Please log in to open packs");
                return "redirect:/login";
            }


            List<Coin> coinsReceived = collectionService.openPackForUser(username, packType);
            redirectAttributes.addFlashAttribute("coinsReceived", coinsReceived);
            redirectAttributes.addFlashAttribute("successMessage", "Pack opened successfully!");

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error opening pack: " + e.getMessage());
        }
        return "redirect:/game";
    }

    @GetMapping("/leaderboard")
    public String showLeaderboard(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            return "redirect:/login";
        }

        // Get top users by collection worth
        List<User> topUsers = userService.getTopUsersByCollectionWorth(10);

        // Add user role information to model
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("topUsers", topUsers);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("username", authentication.getName());

        return "leaderboard";
    }

    @GetMapping("/debug/pack-contents/{packType}")
    @ResponseBody
    public List<Coin> getPackContents(@PathVariable PackType packType) {
        return packService.getCoinsByPackType(packType);
    }
}
