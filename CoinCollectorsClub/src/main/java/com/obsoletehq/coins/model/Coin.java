package com.obsoletehq.coins.model;

import com.obsoletehq.coins.game.PackType;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

@Entity
public class Coin {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private UUID id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "coin_family_id")
    private CoinFamily coinFamily;

    @Column(name = "denomination")
    private int denomination;

    @Column(name = "probability")
    private double probability;

    @Column(name = "legendary")
    private boolean legendary;

    @Enumerated(EnumType.STRING)
    @Column(name = "pack", nullable = false)
    private PackType packType;

    public Coin() {
    }

    public Coin(String name, CoinFamily coinFamily, int denomination, double probability, boolean legendary, PackType packType) {
        this.name = name;
        this.coinFamily = coinFamily;
        this.denomination = denomination;
        this.probability = probability;
        this.legendary = legendary;
        this.packType = packType;
    }


    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public CoinFamily getCoinFamily() {
        return coinFamily;
    }
    public void setCoinFamily(CoinFamily coinFamily) {
        this.coinFamily = coinFamily;
    }

    public int getDenomination() {
        return denomination;
    }
    public void setDenomination(int denomination) {
        this.denomination = denomination;
    }

    public double getProbability() {
        return probability;
    }
    public void setProbability(double probability) {
        this.probability = probability;
    }
    public boolean isLegendary() {
        return legendary;
    }
    public void setLegendary(boolean legendary) {
        this.legendary = legendary;
    }

    public PackType getPackType() {
        return packType;
    }
    public void setPackType(PackType packType) {
        this.packType = packType;
    }
}
