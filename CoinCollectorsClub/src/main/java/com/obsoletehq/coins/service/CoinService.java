package com.obsoletehq.coins.service;

import com.obsoletehq.coins.model.Coin;
import com.obsoletehq.coins.model.CoinFamily;
import com.obsoletehq.coins.repository.CoinFamilyRepo;
import com.obsoletehq.coins.repository.CoinRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

@Service
public class CoinService {
    @Autowired
    private CoinRepo coinRepo;

    @Autowired
    private CoinFamilyRepo coinFamilyRepo;

    @PostConstruct
    public void initializeCoins() {
        if (coinRepo.count() == 0 && coinFamilyRepo.count() == 0) {
            // Create families
            CoinFamily ancientCoins = new CoinFamily("ANCIENT_COINS", "Ancient Coins");
            CoinFamily treasureDoubloons = new CoinFamily("TREASURE_DOUBLOONS", "Treasure Doubloons");
            CoinFamily mysticSet = new CoinFamily("MYSTIC_SET", "Mystic Set");
            CoinFamily modernMoney = new CoinFamily("MODERN_MONEY", "Modern Money");
            CoinFamily legendary = new CoinFamily("LEGENDARY", "Legendary Coins");

            // Save families
            coinFamilyRepo.save(ancientCoins);
            coinFamilyRepo.save(treasureDoubloons);
            coinFamilyRepo.save(mysticSet);
            coinFamilyRepo.save(modernMoney);
            coinFamilyRepo.save(legendary);

            // Create and save coins with their respective families
            // Ancient Coins
            coinRepo.save(new Coin("Bronze Drachma", ancientCoins, 100, 0.4, false));
            coinRepo.save(new Coin("Silver Denarius", ancientCoins, 250, 0.3, false));
            coinRepo.save(new Coin("Gold Solidus", ancientCoins, 500, 0.2, false));

            // Treasure Doubloons
            coinRepo.save(new Coin("Copper Doubloon", treasureDoubloons, 150, 0.4, false));
            coinRepo.save(new Coin("Silver Doubloon", treasureDoubloons, 300, 0.3, false));
            coinRepo.save(new Coin("Golden Doubloon", treasureDoubloons, 600, 0.2, false));

            // Mystic Set
            coinRepo.save(new Coin("Enchanted Coin", mysticSet, 400, 0.3, false));
            coinRepo.save(new Coin("Cursed Coin", mysticSet, 450, 0.2, false));
            coinRepo.save(new Coin("Celestial Coin", mysticSet, 500, 0.1, false));

            // Modern Money
            coinRepo.save(new Coin("Penny", modernMoney, 1, 0.9, false));
            coinRepo.save(new Coin("Nickel", modernMoney, 5, 0.8, false));
            coinRepo.save(new Coin("Dime", modernMoney, 10, 0.7, false));
            coinRepo.save(new Coin("Quarter", modernMoney, 25, 0.6, false));

            // Legendary Coins
            coinRepo.save(new Coin("Dragon's Hoard Coin", legendary, 1000, 0.05, true));
            coinRepo.save(new Coin("Philosopher's Stone Coin", legendary, 1500, 0.03, true));
            coinRepo.save(new Coin("Eternal Flame Coin", legendary, 2000, 0.01, true));
        }

    }
}
