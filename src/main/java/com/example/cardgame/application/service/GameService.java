package com.example.cardgame.application.service;

import com.example.cardgame.application.port.IGameService;
import com.example.cardgame.domain.exception.GameNotFoundException;
import com.example.cardgame.domain.model.Game;
import com.example.cardgame.domain.repository.IGameRepository;
import com.example.cardgame.domain.util.ErrorMessage;
import com.example.cardgame.domain.util.Validations;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class GameService implements IGameService {

    private final IGameRepository gameRepository;
    private final Random random;

    public GameService(IGameRepository gameRepository, Random random) {
        this.gameRepository = gameRepository;
        this.random = random;
    }

    public Game createGame() {
        var game = Game.empty();
        return gameRepository.save(game);
    }

    public void deleteGame(String gameId) {
        Validations.requireNonNull(gameId, ErrorMessage.GAME_ID_NULL);
        gameRepository.deleteByIdOrThrow(gameId);
    }

    public void shuffleGameDeck(String gameId) {
        Validations.requireNonNull(gameId, ErrorMessage.GAME_ID_NULL);
        var result = gameRepository.updateAtomically(gameId, game ->
            game.withGameDeck(game.getGameDeck().shuffle(random))
        );
        if (result.isEmpty()) {
            throw new GameNotFoundException(gameId);
        }
    }
}
