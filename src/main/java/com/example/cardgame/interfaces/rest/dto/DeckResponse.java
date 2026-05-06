package com.example.cardgame.interfaces.rest.dto;

import com.example.cardgame.domain.model.Deck;

public record DeckResponse(
    String id,
    int size
) {
    public static DeckResponse from(Deck deck) {
        return new DeckResponse(deck.getId(), deck.getSize());
    }
}
