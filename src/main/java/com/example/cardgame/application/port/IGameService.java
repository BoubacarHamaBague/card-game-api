package com.example.cardgame.application.port;

import com.example.cardgame.domain.model.Game;

public interface IGameService {
    Game createGame();
    void deleteGame(String gameId);
    void shuffleGameDeck(String gameId);
}
