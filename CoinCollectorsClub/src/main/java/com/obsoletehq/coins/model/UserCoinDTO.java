package com.obsoletehq.coins.model;

import java.math.BigDecimal;
import java.util.*;

public class UserCoinDTO {
    private User user;
    private Map<Coin, Integer> coinDetails;
    private BigDecimal calculatedWorth;
    private Set<String> completedFamilies; // Track which families are complete

    public UserCoinDTO(User user) {
        this.user = user;
        this.coinDetails = new HashMap<>();
        this.calculatedWorth = BigDecimal.ZERO;
        this.completedFamilies = new HashSet<>();
    }

    public int getTotalCoins() {
        return coinDetails.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public int getUniqueCoins() {
        return coinDetails.keySet().size();
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Map<Coin, Integer> getCoinDetails() {
        return coinDetails;
    }

    public void setCoinDetails(Map<Coin, Integer> coinDetails) {
        this.coinDetails = coinDetails;
    }

    public void addCoinDetail(Coin coin, Integer quantity) {
        this.coinDetails.put(coin, quantity);
    }

    public BigDecimal getCalculatedWorth() {
        return calculatedWorth;
    }

    public void setCalculatedWorth(BigDecimal calculatedWorth) {
        this.calculatedWorth = calculatedWorth;
    }

    public Set<String> getCompletedFamilies() {
        return completedFamilies;
    }

    public void addCompletedFamily(String familyName) {
        this.completedFamilies.add(familyName);
    }

    public boolean hasCompletedFamily(String familyName) {
        return completedFamilies.contains(familyName);
    }
}