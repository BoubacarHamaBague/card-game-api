package com.example.cardgame.domain.model;

import java.util.List;

public record DetailedCardsResult(List<Card> cards) {
    public DetailedCardsResult {
        cards = List.copyOf(cards);
    }
}
