package com.example.cardgame.application.service;

import com.example.cardgame.application.port.IGameStatisticsService;
import com.example.cardgame.domain.exception.GameNotFoundException;
import com.example.cardgame.domain.model.Card;
import com.example.cardgame.domain.model.Player;
import com.example.cardgame.domain.repository.IGameRepository;
import com.example.cardgame.domain.util.ErrorMessage;
import com.example.cardgame.domain.util.Validations;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class GameStatisticsService implements IGameStatisticsService {

    private final IGameRepository gameRepository;

    public GameStatisticsService(IGameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Player> getPlayersRanking(String gameId) {
        Validations.requireNonNull(gameId, ErrorMessage.GAME_ID_NULL);
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId))
                .getPlayersSortedByHandValueDesc();
    }

    public Map<String, Integer> getRemainingSuitCount(String gameId) {
        Validations.requireNonNull(gameId, ErrorMessage.GAME_ID_NULL);
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId))
                .getGameDeck().countBySuit().counts();
    }

    public List<Card> getRemainingCardsDetail(String gameId) {
        Validations.requireNonNull(gameId, ErrorMessage.GAME_ID_NULL);
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId))
                .getGameDeck().detailedRemainingCards().cards();
    }
}
