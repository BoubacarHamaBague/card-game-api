package com.example.cardgame.interfaces.rest.dto;

import com.example.cardgame.domain.model.Card;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "A single playing card")
public record CardResponse(
    @Schema(description = "Card suit", example = "Hearts") String suit,
    @Schema(description = "Card rank", example = "King") String rank,
    @Schema(description = "Face value of the card (Ace=1, 2-10, Jack=11, Queen=12, King=13)", example = "13") int value
) {
    public static CardResponse from(Card card) {
        return new CardResponse(
            card.getSuit().getDisplayName(),
            card.getRank().getDisplayName(),
            card.getValue()
        );
    }
}
