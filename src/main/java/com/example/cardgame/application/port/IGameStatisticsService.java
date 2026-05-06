package com.example.cardgame.application.port;

import com.example.cardgame.domain.model.Card;
import com.example.cardgame.domain.model.Player;
import java.util.List;
import java.util.Map;

public interface IGameStatisticsService {
    List<Player> getPlayersRanking(String gameId);
    Map<String, Integer> getRemainingSuitCount(String gameId);
    List<Card> getRemainingCardsDetail(String gameId);
}
