package com.obsoletehq.coins.service;

import com.obsoletehq.coins.game.PackType;
import com.obsoletehq.coins.model.Coin;
import com.obsoletehq.coins.model.Pack;
import com.obsoletehq.coins.repository.CoinRepository;
import com.obsoletehq.coins.repository.PackRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class PackService {

    @Autowired
    private PackRepository packRepository;

    @Autowired
    private CoinRepository coinRepository;

    private final Random random = new Random();

    public List<Coin> getCoinsByPackType(PackType packType) {
        return coinRepository.findByPackType(packType);
    }

    public List<Pack> getAvailablePacks() {
        return packRepository.findAll();
    }

    public List<Coin> openPack(PackType packType) {
        List<Coin> packContents = new ArrayList<>();
        int numCoins = getPackSize(packType);

        for (int i = 0; i < numCoins; i++) {
            Coin selectedCoin = selectRandomCoin(packType);
            if (selectedCoin != null) {
                packContents.add(selectedCoin);
            }
        }

        return packContents;
    }

    private Coin selectRandomCoin(PackType packType) {
        List<Coin> availableCoins = getAllCoinsForPack(packType);

        if (availableCoins.isEmpty()) {
            return null;
        }

        double totalProbability = availableCoins.stream()
                .mapToDouble(Coin::getProbability)
                .sum();

        double randomValue = random.nextDouble() * totalProbability;
        double currentProbability = 0;

        for (Coin coin : availableCoins) {
            currentProbability += coin.getProbability();
            if (randomValue <= currentProbability) {
                return coin;
            }
        }

        return availableCoins.get(random.nextInt(availableCoins.size()));
    }

    public int getPackSize(PackType packType) {
        switch (packType) {
            case COMMON:
                return 3;
            case PREMIUM:
                return 3;
            case  LEGENDARY:
                return 3;
            default:
                return 0;
        }
    }

    private List<Coin> getAllCoinsForPack(PackType packType) {
        List<Coin> coins = new ArrayList<>();

        switch (packType) {
            case LEGENDARY:
                coins.addAll(coinRepository.findByPackType(PackType.LEGENDARY));
                break;
            case PREMIUM:
                coins.addAll(coinRepository.findByPackType(PackType.PREMIUM));
                break;
            case COMMON:
                coins.addAll(coinRepository.findByPackType(PackType.COMMON));
                break;
        }

        return coins;
    }


    @PostConstruct
    public void initializePacks() {
        if (packRepository.count() == 0) {
            packRepository.save(new Pack(PackType.COMMON, "Common Pack", "Basic coin pack with common coins", 100));
            packRepository.save(new Pack(PackType.PREMIUM, "Uncommon Pack", "Better coins with uncommon rarities", 250));
            packRepository.save(new Pack(PackType.LEGENDARY, "Legendary Pack", "Super rare and cool coins!", 500));
        }
    }
}
