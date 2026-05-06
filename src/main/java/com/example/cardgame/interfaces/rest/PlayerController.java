package com.example.cardgame.interfaces.rest;

import com.example.cardgame.application.port.ICardDealingService;
import com.example.cardgame.application.port.IGameStatisticsService;
import com.example.cardgame.application.port.IPlayerService;
import com.example.cardgame.interfaces.rest.dto.DealCardsResponse;
import com.example.cardgame.interfaces.rest.dto.PlayerHandResponse;
import com.example.cardgame.interfaces.rest.dto.PlayerRankingResponse;
import com.example.cardgame.interfaces.rest.dto.PlayerResponse;
import com.example.cardgame.interfaces.rest.requests.AddPlayerRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Players")
@Validated
@RestController
@RequestMapping("/api/games/{gameId}/players")
public class PlayerController {

    private static final String DEAL_PATH = "/{playerId}/deal";
    private static final String GET_CARDS_PATH = "/{playerId}/cards";
    private static final String RANKING_PATH = "/ranking";

    private final IPlayerService playerService;
    private final ICardDealingService cardDealingService;
    private final IGameStatisticsService gameStatisticsService;

    public PlayerController(IPlayerService playerService, ICardDealingService cardDealingService, IGameStatisticsService gameStatisticsService) {
        this.playerService = playerService;
        this.cardDealingService = cardDealingService;
        this.gameStatisticsService = gameStatisticsService;
    }

    @PostMapping
    @Operation(summary = "Add a player to the game")
    @ApiResponse(responseCode = "201", description = "Player added")
    public ResponseEntity<PlayerResponse> addPlayer(
            @PathVariable @NotBlank String gameId,
            @Valid @RequestBody AddPlayerRequest request) {
        var player = playerService.addPlayer(gameId, request.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(PlayerResponse.from(player));
    }

    @DeleteMapping("/{playerId}")
    @Operation(summary = "Remove a player from the game")
    @ApiResponse(responseCode = "204", description = "Player removed")
    public ResponseEntity<Void> removePlayer(
            @PathVariable @NotBlank String gameId,
            @PathVariable @NotBlank String playerId) {
        playerService.removePlayer(gameId, playerId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(DEAL_PATH)
    @Operation(summary = "Deal cards to a player")
    @ApiResponse(responseCode = "200", description = "Cards dealt")
    public ResponseEntity<DealCardsResponse> dealCards(
            @PathVariable @NotBlank String gameId,
            @PathVariable @NotBlank String playerId,
            @RequestParam(required = true) @Min(0) int count) {
        var dealt = cardDealingService.dealCards(gameId, playerId, count);
        return ResponseEntity.ok(DealCardsResponse.from(dealt));
    }

    @GetMapping(GET_CARDS_PATH)
    @Operation(summary = "Get a player's hand")
    @ApiResponse(responseCode = "200", description = "Player's hand retrieved")
    public ResponseEntity<PlayerHandResponse> getPlayerCards(
            @PathVariable @NotBlank String gameId,
            @PathVariable @NotBlank String playerId) {
        var player = playerService.getPlayer(gameId, playerId);
        return ResponseEntity.ok(PlayerHandResponse.from(player));
    }

    @GetMapping(RANKING_PATH)
    @Operation(summary = "Get players ranking by hand value")
    @ApiResponse(responseCode = "200", description = "Players ranking retrieved")
    public ResponseEntity<PlayerRankingResponse> getRanking(@PathVariable @NotBlank String gameId) {
        var ranking = gameStatisticsService.getPlayersRanking(gameId);
        return ResponseEntity.ok(PlayerRankingResponse.from(ranking));
    }
}
