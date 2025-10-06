package com.obsoletehq.coins.model;

import com.obsoletehq.coins.game.PackType;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class Pack {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private PackType packType;

    private String name;
    private String description;
    private int price;

    public Pack() {

    }

    public Pack(PackType packType, String name, String description, int price) {
        this.packType = packType;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public PackType getPackType() {
        return packType;
    }
    public void setPackType(PackType packType) {
        this.packType = packType;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
}
