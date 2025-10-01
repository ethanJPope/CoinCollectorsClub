package com.obsoletehq.coins.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(name = "collection_worth")
    private BigDecimal collectionWorth;

    @ElementCollection
    @CollectionTable(name = "user_coins",
            joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "coin_id")
    @Column(name = "quantity")
    private Map<UUID, Integer> coinCollection;

    @Column(name = "total_searches")
    private int totalSearches;

    @Column(name = "completed_sets")
    private int completedSets;

    @Column(name = "last_search_time")
    private LocalDateTime lastSearchTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "active")
    private boolean active;

    public User() {
        this.coinCollection = new HashMap<>();
        this.collectionWorth = BigDecimal.ZERO;
    }

    public User(String username, String password, String email) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.email = email;
        this.collectionWorth = BigDecimal.ZERO;
        this.coinCollection = new HashMap<>();
        this.totalSearches = 0;
        this.completedSets = 0;
        this.lastSearchTime = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.active = true;
    }

    public void addCoin(Coin coin) {
        addCoin(coin.getId());
    }

    public void addCoin(UUID coinId) {
        coinCollection.merge(coinId, 1, Integer::sum);
    }

    // Getters and setters
    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public Map<UUID, Integer> getCoinCollection() { return coinCollection; }
    public BigDecimal getCollectionWorth() { return collectionWorth; }
    public void setCollectionWorth(BigDecimal worth) { this.collectionWorth = worth; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
