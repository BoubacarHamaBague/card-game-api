package com.example.cardgame.application.port;

import com.example.cardgame.domain.model.Deck;
import com.example.cardgame.domain.model.Game;

public interface IDeckService {
    Deck createDeck();
    Game addDeckToGame(String gameId, String deckId);
}
