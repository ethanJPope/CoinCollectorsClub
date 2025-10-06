package com.obsoletehq.coins.model;

import com.obsoletehq.coins.game.*;
import jakarta.persistence.*;

@Entity
@Table(name = "user_coins")
public class UserCoin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coin_id", nullable = false)
    private Coin coin;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    @Column(name = "obtained_from_pack")
    @Enumerated(EnumType.STRING)
    private PackType obtainedFromPack;

    // Constructors
    public UserCoin() {}

    public UserCoin(User user, Coin coin, PackType obtainedFromPack) {
        this.user = user;
        this.coin = coin;
        this.obtainedFromPack = obtainedFromPack;
        this.quantity = 1;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Coin getCoin() { return coin; }
    public void setCoin(Coin coin) { this.coin = coin; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public PackType getObtainedFromPack() { return obtainedFromPack; }
    public void setObtainedFromPack(PackType obtainedFromPack) { this.obtainedFromPack = obtainedFromPack; }
}
