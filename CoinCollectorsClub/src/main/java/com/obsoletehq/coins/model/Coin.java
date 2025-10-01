package com.obsoletehq.coins.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "coins")
public class Coin {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "family_id", nullable = false)
    private CoinFamily family;

    @Column(name = "base_value")
    private int baseValue;

    @Column(name = "rarity_weight")
    private double rarityWeight;

    @Column(name = "special")
    private boolean special;

    public Coin() {
        this.id = UUID.randomUUID();
    }

    public Coin(String name, CoinFamily family, int baseValue, double rarityWeight, boolean special) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.family = family;
        this.baseValue = baseValue;
        this.rarityWeight = rarityWeight;
        this.special = special;
    }

    // Getters
    public UUID getId() { return id; }
    public String getName() { return name; }
    public CoinFamily getFamily() { return family; }
    public int getBaseValue() { return baseValue; }
    public double getRarityWeight() { return rarityWeight; }
    public boolean isSpecial() { return special; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setFamily(CoinFamily family) { this.family = family; }
    public void setBaseValue(int baseValue) { this.baseValue = baseValue; }
    public void setRarityWeight(double rarityWeight) { this.rarityWeight = rarityWeight; }
    public void setSpecial(boolean special) { this.special = special; }
}
