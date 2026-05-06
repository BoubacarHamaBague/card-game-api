package com.example.cardgame.application.service;

import com.example.cardgame.application.port.IDeckService;
import com.example.cardgame.domain.exception.DeckNotFoundException;
import com.example.cardgame.domain.exception.GameNotFoundException;
import com.example.cardgame.domain.model.Deck;
import com.example.cardgame.domain.model.Game;
import com.example.cardgame.domain.repository.IDeckRepository;
import com.example.cardgame.domain.repository.IGameRepository;
import com.example.cardgame.domain.util.ErrorMessage;
import com.example.cardgame.domain.util.Validations;
import org.springframework.stereotype.Service;

@Service
public class DeckService implements IDeckService {

    private final IGameRepository gameRepository;
    private final IDeckRepository deckRepository;

    public DeckService(IGameRepository gameRepository, IDeckRepository deckRepository) {
        this.gameRepository = gameRepository;
        this.deckRepository = deckRepository;
    }

    public Deck createDeck() {
        return deckRepository.save(Deck.standardDeck());
    }

    public Game addDeckToGame(String gameId, String deckId) {
        Validations.requireNonNull(gameId, ErrorMessage.GAME_ID_NULL);
        Validations.requireNonNull(deckId, ErrorMessage.DECK_ID_NULL);

        gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException(gameId));

        var deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new DeckNotFoundException(deckId));

        var result = gameRepository.updateAtomically(gameId, game ->
                game.withGameDeck(game.getGameDeck().addDeck(deck)));

        return result.orElseThrow(() -> new GameNotFoundException(gameId));
    }
}
