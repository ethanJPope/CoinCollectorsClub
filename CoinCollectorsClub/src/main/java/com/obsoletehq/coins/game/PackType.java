package com.obsoletehq.coins.game;

public enum PackType {
    COMMON("Common Pack"),
    PREMIUM("Premium Pack"),
    LEGENDARY("Legendary Pack");

    private final String displayName;

    PackType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
