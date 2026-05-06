package com.example.cardgame.domain.model;

import com.example.cardgame.domain.exception.InvalidCardException;
import java.util.Objects;

public final class Card {
    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        if (suit == null) throw InvalidCardException.nullSuit();
        if (rank == null) throw InvalidCardException.nullRank();

        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public int getValue() {
        return rank.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card card)) return false;
        return suit == card.suit && rank == card.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }

    @Override
    public String toString() {
        return rank.getDisplayName() + " of " + suit.getDisplayName();
    }
}
