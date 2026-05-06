package com.example.cardgame.domain.model;

public enum Suit {
    HEARTS("Hearts"),
    SPADES("Spades"),
    CLUBS("Clubs"),
    DIAMONDS("Diamonds");

    private final String displayName;

    Suit(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
