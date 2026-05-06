package com.example.cardgame.interfaces.rest.dto;

import com.example.cardgame.domain.model.Game;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Current state of a game")
public record GameResponse(
    @Schema(description = "Unique game identifier", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479") String id,
    @Schema(description = "Number of cards remaining in the game deck", example = "47") int remainingCards,
    @Schema(description = "Number of players in the game", example = "3") int playerCount,
    @Schema(description = "Players currently in the game") List<PlayerResponse> players
) {
    public static GameResponse from(Game game) {
        List<PlayerResponse> playerList = game.getPlayers().values().stream()
                .map(PlayerResponse::from)
                .toList();

        return new GameResponse(
            game.getId(),
            game.getGameDeck().getRemainingCount(),
            game.getPlayers().size(),
            playerList
        );
    }
}
