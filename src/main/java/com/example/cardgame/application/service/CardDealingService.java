package com.example.cardgame.application.service;

import com.example.cardgame.application.port.ICardDealingService;
import com.example.cardgame.domain.exception.GameNotFoundException;
import com.example.cardgame.domain.exception.InvalidGameDeckException;
import com.example.cardgame.domain.exception.PlayerNotFoundException;
import com.example.cardgame.domain.model.Card;
import com.example.cardgame.domain.repository.IGameRepository;
import com.example.cardgame.domain.util.ErrorMessage;
import com.example.cardgame.domain.util.Validations;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.stereotype.Service;

@Service
public class CardDealingService implements ICardDealingService {

    private final IGameRepository gameRepository;

    public CardDealingService(IGameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public List<Card> dealCards(String gameId, String playerId, int count) {
        Validations.requireNonNull(gameId, ErrorMessage.GAME_ID_NULL);
        Validations.requireNonNull(playerId, ErrorMessage.PLAYER_ID_NULL);
        if (count < 0) {
            throw InvalidGameDeckException.negativeCount();
        }

        final AtomicReference<List<Card>> dealtCards = new AtomicReference<>(List.of());

        var result = gameRepository.updateAtomically(gameId, game -> {
            if (!game.hasPlayer(playerId)) {
                throw new PlayerNotFoundException(playerId);
            }

            var dealResult = game.getGameDeck().dealToPlayer(count);
            dealtCards.set(dealResult.dealt());

            if (dealResult.dealt().isEmpty()) {
                return game;
            }

            var player = game.getPlayer(playerId);
            var updatedPlayer = player.withAdditionalCards(dealResult.dealt());
            return game.withGameDeck(dealResult.remaining()).withPlayer(updatedPlayer);
        });

        if (result.isEmpty()) {
            throw new GameNotFoundException(gameId);
        }

        return dealtCards.get();
    }

}
