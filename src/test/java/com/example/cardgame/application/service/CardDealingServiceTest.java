package com.example.cardgame.application.service;

import com.example.cardgame.domain.exception.GameNotFoundException;
import com.example.cardgame.domain.exception.PlayerNotFoundException;
import com.example.cardgame.domain.model.Card;
import com.example.cardgame.domain.model.Deck;
import com.example.cardgame.domain.model.Game;
import com.example.cardgame.domain.model.Player;
import com.example.cardgame.domain.repository.IGameRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CardDealingServiceTest {

    private static final String PLAYER_NAME_ALICE = "Alice";
    private static final String NONEXISTENT_ID = "nonexistent";
    private static final String PLAYER_ID = "playerId";
    private static final int CARDS_TO_DEAL = 5;

    private CardDealingService service;

    @Mock
    private IGameRepository gameRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new CardDealingService(gameRepository);
    }

    @Test
    void givenGameWithDeckAndPlayerAndCardCount_whenDealCards_thenCardsAreDealt() {
        // Arrange
        Game game = Game.empty().withGameDeck(
                Game.empty().getGameDeck().addDeck(Deck.standardDeck()));
        Player player = new Player(PLAYER_NAME_ALICE);
        Game gameWithPlayer = game.addPlayer(player);

        when(gameRepository.updateAtomically(eq(game.getId()), any()))
                .thenAnswer(inv -> {
                    var updater = (java.util.function.Function<Game, Game>) inv.getArgument(1);
                    return Optional.of(updater.apply(gameWithPlayer));
                });

        // Act
        List<Card> result = service.dealCards(game.getId(), player.getId(), CARDS_TO_DEAL);

        // Assert
        assertThat(result).hasSize(CARDS_TO_DEAL);
    }

    @Test
    void givenNonexistentGameIdAndPlayerId_whenDealCards_thenThrowGameNotFoundException() {
        // Arrange
        when(gameRepository.updateAtomically(eq(NONEXISTENT_ID), any()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.dealCards(NONEXISTENT_ID, PLAYER_ID, CARDS_TO_DEAL))
                .isInstanceOf(GameNotFoundException.class)
                .hasMessage(new GameNotFoundException(NONEXISTENT_ID).getMessage());
    }

    @Test
    void givenGameWithPlayerButNotEnoughCards_whenDealCards_thenReturnEmptyList() {
        // Arrange
        Game game = Game.empty();
        Player player = new Player(PLAYER_NAME_ALICE);
        Game gameWithPlayer = game.addPlayer(player);

        when(gameRepository.updateAtomically(eq(game.getId()), any()))
                .thenAnswer(inv -> {
                    var updater = (java.util.function.Function<Game, Game>) inv.getArgument(1);
                    return Optional.of(updater.apply(gameWithPlayer));
                });

        // Act
        List<Card> result = service.dealCards(game.getId(), player.getId(), CARDS_TO_DEAL);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void givenGameWithoutPlayer_whenDealCards_thenThrowPlayerNotFoundException() {
        // Arrange
        Game game = Game.empty();
        when(gameRepository.updateAtomically(eq(game.getId()), any()))
                .thenAnswer(inv -> {
                    var updater = (java.util.function.Function<Game, Game>) inv.getArgument(1);
                    return Optional.of(updater.apply(game));
                });

        // Act & Assert
        assertThatThrownBy(() -> service.dealCards(game.getId(), PLAYER_ID, CARDS_TO_DEAL))
                .isInstanceOf(PlayerNotFoundException.class)
                .hasMessage(new PlayerNotFoundException(PLAYER_ID).getMessage());
    }

}
