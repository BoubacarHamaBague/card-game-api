package com.example.cardgame.interfaces.rest;

import com.example.cardgame.application.port.IGameStatisticsService;
import com.example.cardgame.interfaces.rest.dto.RemainingCardsResponse;
import com.example.cardgame.interfaces.rest.dto.SuitCountResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Statistics")
@RestController
@RequestMapping("/api/games/{gameId}/remaining")
public class StatisticsController {

    private final IGameStatisticsService statisticsService;

    public StatisticsController(IGameStatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/suits")
    @Operation(summary = "Get remaining cards count by suit")
    @ApiResponse(responseCode = "200", description = "Suit counts retrieved")
    public ResponseEntity<SuitCountResponse> getRemainingSuitCount(@PathVariable String gameId) {
        var counts = statisticsService.getRemainingSuitCount(gameId);
        return ResponseEntity.ok(SuitCountResponse.from(counts));
    }

    @GetMapping("/cards")
    @Operation(summary = "Get detailed remaining cards list")
    @ApiResponse(responseCode = "200", description = "Remaining cards retrieved")
    public ResponseEntity<RemainingCardsResponse> getRemainingCards(@PathVariable String gameId) {
        var cards = statisticsService.getRemainingCardsDetail(gameId);
        return ResponseEntity.ok(RemainingCardsResponse.from(cards));
    }
}
