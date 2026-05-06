package com.example.cardgame.interfaces.rest.dto;

import com.example.cardgame.domain.model.Card;
import java.util.List;

public record DealCardsResponse(
    List<CardResponse> dealt,
    int totalDealt
) {
    public static DealCardsResponse from(List<Card> cards) {
        List<CardResponse> responses = cards.stream()
                .map(CardResponse::from)
                .toList();
        return new DealCardsResponse(responses, responses.size());
    }
}
