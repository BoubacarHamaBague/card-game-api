package com.example.cardgame.domain.model;

import com.example.cardgame.domain.exception.InvalidDeckException;
import com.example.cardgame.domain.util.IIdentifiable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class Deck implements IIdentifiable {
    private final String id;
    private final List<Card> cards;

    public Deck(String id, List<Card> cards) {
        if (id == null) throw InvalidDeckException.nullId();
        if (cards == null) throw InvalidDeckException.nullCards();

        this.id = id;
        this.cards = Collections.unmodifiableList(new ArrayList<>(cards));
    }

    public String getId() {
        return id;
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getSize() {
        return cards.size();
    }

    public static Deck standardDeck() {
        List<Card> cards = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
        return new Deck(UUID.randomUUID().toString(), cards);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Deck deck)) return false;
        return id.equals(deck.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Deck{id='" + id + "', cardCount=" + cards.size() + "}";
    }
}
