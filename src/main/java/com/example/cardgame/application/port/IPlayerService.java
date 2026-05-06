package com.example.cardgame.application.port;

import com.example.cardgame.domain.model.Player;

public interface IPlayerService {
    Player addPlayer(String gameId, String playerName);
    void removePlayer(String gameId, String playerId);
    Player getPlayer(String gameId, String playerId);
}
