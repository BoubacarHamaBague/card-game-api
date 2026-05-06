package com.example.cardgame.domain.model;

import com.example.cardgame.domain.exception.InvalidPlayerException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class Player {
    private final String id;
    private final String name;
    private final List<Card> hand;

    public Player(String name) {
        this(UUID.randomUUID().toString(), name, new ArrayList<>());
    }

    public Player(String id, String name, List<Card> hand) {
        if (id == null) throw InvalidPlayerException.nullId();
        if (name == null) throw InvalidPlayerException.nullName();
        if (hand == null) throw InvalidPlayerException.nullHand();

        this.id = id;
        this.name = name;
        this.hand = Collections.unmodifiableList(new ArrayList<>(hand));
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public int getHandValue() {
        return hand.stream().mapToInt(Card::getValue).sum();
    }

    public Player withAdditionalCards(List<Card> newCards) {
        if (newCards == null) throw InvalidPlayerException.nullNewCards();

        List<Card> combined = new ArrayList<>(hand);
        combined.addAll(newCards);
        return new Player(id, name, combined);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        return id.equals(player.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Player{id='" + id + "', name='" + name + "', handValue=" + getHandValue() + "}";
    }
}
