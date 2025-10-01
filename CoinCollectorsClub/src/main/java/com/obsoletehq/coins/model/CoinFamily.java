package com.obsoletehq.coins.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "coin_families")
public class CoinFamily {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String displayName;

    public CoinFamily(){}

    public CoinFamily(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDisplayName() { return displayName; }
    public void setName(String name) { this.name = name; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
}
