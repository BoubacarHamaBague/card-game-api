package com.example.cardgame.interfaces.rest.dto;

import com.example.cardgame.domain.model.Player;
import java.util.List;
import java.util.stream.IntStream;

public record PlayerRankingResponse(
    List<PlayerRankItem> ranking
) {
    public static PlayerRankingResponse from(List<Player> players) {
        List<PlayerRankItem> items = IntStream.range(0, players.size())
            .mapToObj(i -> new PlayerRankItem(
                i + 1,
                players.get(i).getId(),
                players.get(i).getName(),
                players.get(i).getHandValue(),
                players.get(i).getHand().size()
            ))
            .toList();
        return new PlayerRankingResponse(items);
    }

    public record PlayerRankItem(
        int position,
        String playerId,
        String playerName,
        int handValue,
        int cardCount
    ) {}
}
