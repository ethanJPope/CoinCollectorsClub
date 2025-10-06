package com.obsoletehq.coins.service;

import com.obsoletehq.coins.game.PackType;
import com.obsoletehq.coins.model.*;
import com.obsoletehq.coins.repository.CoinFamilyRepository;
import com.obsoletehq.coins.repository.CoinRepository;
import com.obsoletehq.coins.repository.UserCoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CoinService {

    @Autowired
    private CoinRepository coinRepo;
    @Autowired
    private CoinFamilyRepository coinFamilyRepository;
    @Autowired
    private UserCoinRepository userCoinRepository;

    public List<Coin> getAllCoins() {
        return coinRepo.findAll();
    }
    public UserCoinDTO getUserCollection(User user) {
        List<UserCoin> userCoins = userCoinRepository.findByUser(user);  // Changed this line

        UserCoinDTO userCoinDTO = new UserCoinDTO(user);

        for (UserCoin userCoin : userCoins) {
            userCoinDTO.addCoinDetail(userCoin.getCoin(), userCoin.getQuantity());
        }

        return userCoinDTO;
    }

    public String getCoinName(UUID coinId) {
        Optional<Coin> coinOpt = coinRepo.findById(coinId);
        if (coinOpt.isPresent()) {
            return coinOpt.get().getName();
        }
        throw new RuntimeException("Coin not found with id " + coinId);
    }

    @PostConstruct
    public void initializeCoins() {
        // Create and save coins with their respective family types
        if (coinFamilyRepository.count() == 0) {
            coinFamilyRepository.save(new CoinFamily("ancient", "Ancient Coins"));
            coinFamilyRepository.save(new CoinFamily("treasure_doubloons", "Treasure Doubloons"));
            coinFamilyRepository.save(new CoinFamily("mystic_set", "Mystic Set"));
            coinFamilyRepository.save(new CoinFamily("modern_money", "Modern Money"));
            coinFamilyRepository.save(new CoinFamily("legendary", "Legendary Coins"));
        }
        if (coinRepo.count() == 0) {
            coinRepo.save(new Coin("Bronze Drachma", coinFamilyRepository.findByName("ancient"), 100, 0.4, false, PackType.COMMON));
            coinRepo.save(new Coin("Silver Denarius", coinFamilyRepository.findByName("ancient"), 250, 0.3, false, PackType.PREMIUM));
            coinRepo.save(new Coin("Gold Solidus", coinFamilyRepository.findByName("ancient"), 500, 0.2, false, PackType.PREMIUM));

            coinRepo.save(new Coin("Copper Doubloon", coinFamilyRepository.findByName("treasure_doubloons"), 150, 0.4, false, PackType.PREMIUM));
            coinRepo.save(new Coin("Silver Doubloon", coinFamilyRepository.findByName("treasure_doubloons"), 300, 0.3, false, PackType.PREMIUM));
            coinRepo.save(new Coin("Golden Doubloon", coinFamilyRepository.findByName("treasure_doubloons"), 600, 0.2, false, PackType.PREMIUM));

            coinRepo.save(new Coin("Enchanted Coin", coinFamilyRepository.findByName("mystic_set"), 400, 0.3, false, PackType.LEGENDARY));
            coinRepo.save(new Coin("Cursed Coin", coinFamilyRepository.findByName("mystic_set"), 450, 0.2, false, PackType.LEGENDARY));
            coinRepo.save(new Coin("Celestial Coin", coinFamilyRepository.findByName("mystic_set"), 500, 0.1, false, PackType.LEGENDARY));

            coinRepo.save(new Coin("Penny", coinFamilyRepository.findByName("modern_money"), 1, 0.9, false, PackType.COMMON));
            coinRepo.save(new Coin("Nickel", coinFamilyRepository.findByName("modern_money"), 5, 0.8, false, PackType.COMMON));
            coinRepo.save(new Coin("Dime", coinFamilyRepository.findByName("modern_money"), 10, 0.7, false, PackType.COMMON));
            coinRepo.save(new Coin("Quarter", coinFamilyRepository.findByName("modern_money"), 25, 0.6, false, PackType.COMMON));

            coinRepo.save(new Coin("Dragon's Hoard Coin", coinFamilyRepository.findByName("legendary"), 1000, 0.05, true, PackType.LEGENDARY));
            coinRepo.save(new Coin("Philosopher's Stone Coin", coinFamilyRepository.findByName("legendary"), 1500, 0.03, true, PackType.LEGENDARY));
            coinRepo.save(new Coin("Eternal Flame Coin", coinFamilyRepository.findByName("legendary"), 2000, 0.01, true, PackType.LEGENDARY));
        }
    }
}
