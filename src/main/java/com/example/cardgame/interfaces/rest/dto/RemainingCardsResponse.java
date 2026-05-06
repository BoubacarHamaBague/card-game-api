package com.example.cardgame.interfaces.rest.dto;

import com.example.cardgame.domain.model.Card;
import java.util.List;

public record RemainingCardsResponse(
    int total,
    List<CardResponse> cards
) {
    public static RemainingCardsResponse from(List<Card> cards) {
        List<CardResponse> responses = cards.stream()
                .map(CardResponse::from)
                .toList();
        return new RemainingCardsResponse(responses.size(), responses);
    }
}
