package com.example.cardgame.interfaces.rest;

import com.example.cardgame.application.port.IDeckService;
import com.example.cardgame.interfaces.rest.dto.DeckResponse;
import com.example.cardgame.interfaces.rest.dto.GameResponse;
import com.example.cardgame.interfaces.rest.requests.AddDeckRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Deck")
@Validated
@RestController
@RequestMapping("/api")
public class DeckController {

    private final IDeckService deckService;

    public DeckController(IDeckService deckService) {
        this.deckService = deckService;
    }

    @PostMapping("/decks")
    @Operation(summary = "Create a new standard deck")
    @ApiResponse(responseCode = "201", description = "Deck created")
    public ResponseEntity<DeckResponse> createDeck() {
        var deck = deckService.createDeck();
        return ResponseEntity.status(HttpStatus.CREATED).body(DeckResponse.from(deck));
    }

    @PostMapping("/games/{gameId}/decks")
    @Operation(summary = "Add a deck to a game")
    @ApiResponse(responseCode = "201", description = "Deck added to game")
    public ResponseEntity<GameResponse> addDeckToGame(
            @PathVariable @NotBlank String gameId,
            @Valid @RequestBody AddDeckRequest request) {
        var game = deckService.addDeckToGame(gameId, request.deckId());
        return ResponseEntity.status(HttpStatus.CREATED).body(GameResponse.from(game));
    }
}
