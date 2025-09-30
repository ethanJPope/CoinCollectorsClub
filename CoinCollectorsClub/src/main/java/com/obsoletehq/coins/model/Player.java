package com.obsoletehq.coins.model;

public class Player {
    private String name;
    private double worth;

    public Player(String name, double worth) {
        this.name = name;
        this.worth = worth;
    }

    public String getName() {
        return name;
    }

    public double getWorth() {
        return worth;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWorth(double worth) {
        this.worth = worth;
    }
}
