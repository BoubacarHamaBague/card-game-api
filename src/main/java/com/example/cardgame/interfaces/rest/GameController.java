package com.example.cardgame.interfaces.rest;

import com.example.cardgame.application.port.IGameService;
import com.example.cardgame.interfaces.rest.dto.GameResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Game")
@Validated
@RestController
@RequestMapping("/api/games")
public class GameController {

    private static final String SHUFFLE_PATH = "/{gameId}/shuffle";

    private final IGameService gameService;

    public GameController(IGameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    @Operation(summary = "Create a new game")
    @ApiResponse(responseCode = "201", description = "Game created")
    public ResponseEntity<GameResponse> createGame() {
        var game = gameService.createGame();
        return ResponseEntity.status(HttpStatus.CREATED).body(GameResponse.from(game));
    }

    @DeleteMapping("/{gameId}")
    @Operation(summary = "Delete a game")
    @ApiResponse(responseCode = "204", description = "Game deleted")
    public ResponseEntity<Void> deleteGame(@PathVariable @NotBlank String gameId) {
        gameService.deleteGame(gameId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(SHUFFLE_PATH)
    @Operation(summary = "Shuffle the game deck")
    @ApiResponse(responseCode = "204", description = "Game deck shuffled")
    public ResponseEntity<Void> shuffleGame(@PathVariable @NotBlank String gameId) {
        gameService.shuffleGameDeck(gameId);
        return ResponseEntity.noContent().build();
    }
}
