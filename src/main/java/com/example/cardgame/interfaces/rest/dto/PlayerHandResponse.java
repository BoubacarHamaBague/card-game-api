package com.example.cardgame.interfaces.rest.dto;

import com.example.cardgame.domain.model.Player;
import java.util.List;

public record PlayerHandResponse(
    String playerId,
    String playerName,
    List<CardResponse> hand,
    int totalValue
) {
    public static PlayerHandResponse from(Player player) {
        List<CardResponse> cards = player.getHand().stream()
                .map(CardResponse::from)
                .toList();

        return new PlayerHandResponse(
            player.getId(),
            player.getName(),
            cards,
            player.getHandValue()
        );
    }
}
