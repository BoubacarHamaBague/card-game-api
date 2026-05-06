package com.example.cardgame.application.service;

import com.example.cardgame.application.port.IPlayerService;
import com.example.cardgame.domain.exception.GameNotFoundException;
import com.example.cardgame.domain.exception.PlayerNotFoundException;
import com.example.cardgame.domain.model.Player;
import com.example.cardgame.domain.repository.IGameRepository;
import com.example.cardgame.domain.util.ErrorMessage;
import com.example.cardgame.domain.util.Validations;
import org.springframework.stereotype.Service;

@Service
public class PlayerService implements IPlayerService {

    private final IGameRepository gameRepository;

    public PlayerService(IGameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Player addPlayer(String gameId, String playerName) {
        Validations.requireNonNull(gameId, ErrorMessage.GAME_ID_NULL);
        Validations.requireNonNull(playerName, ErrorMessage.PLAYER_NAME_NULL);

        var player = new Player(playerName);
        gameRepository.updateAtomically(gameId, game ->
                game.addPlayer(player))
            .orElseThrow(() -> new GameNotFoundException(gameId));
        return player;
    }

    public void removePlayer(String gameId, String playerId) {
        Validations.requireNonNull(gameId, ErrorMessage.GAME_ID_NULL);
        Validations.requireNonNull(playerId, ErrorMessage.PLAYER_ID_NULL);

        var result = gameRepository.updateAtomically(gameId, game -> {
            if (!game.hasPlayer(playerId)) {
                throw new PlayerNotFoundException(playerId);
            }
            return game.removePlayer(playerId);
        });
        result.orElseThrow(() -> new GameNotFoundException(gameId));
    }

    public Player getPlayer(String gameId, String playerId) {
        Validations.requireNonNull(gameId, ErrorMessage.GAME_ID_NULL);
        Validations.requireNonNull(playerId, ErrorMessage.PLAYER_ID_NULL);

        var game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));
        return game.getPlayer(playerId);
    }
}
