package com.example.cardgame.interfaces.rest.dto;

import com.example.cardgame.domain.model.Player;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Player information")
public record PlayerResponse(
    @Schema(description = "Unique player identifier", example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890") String id,
    @Schema(description = "Player display name", example = "Alice") String name,
    @Schema(description = "Total face value of all cards in hand", example = "25") int handValue,
    @Schema(description = "Number of cards currently held", example = "2") int cardCount
) {
    public static PlayerResponse from(Player player) {
        return new PlayerResponse(
            player.getId(),
            player.getName(),
            player.getHandValue(),
            player.getHand().size()
        );
    }
}
