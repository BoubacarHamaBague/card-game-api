package com.example.cardgame.application.service;

import com.example.cardgame.domain.exception.GameNotFoundException;
import com.example.cardgame.domain.model.Deck;
import com.example.cardgame.domain.model.Game;
import com.example.cardgame.domain.repository.IGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GameServiceTest {

    private static final String NONEXISTENT_ID = "nonexistent";

    private GameService service;

    @Mock
    private IGameRepository gameRepository;

    @Mock
    private Random random;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new GameService(gameRepository, random);
    }

    @Test
    void givenRepositoryReturnsGame_whenCreateGame_thenGameIsCreatedWithValidId() {
        // Arrange
        when(gameRepository.save(any(Game.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Game game = service.createGame();

        // Assert
        assertThat(game).isNotNull();
        assertThat(game.getId()).isNotNull();
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    void givenGameExists_whenDeleteGame_thenGameIsDeleted() {
        // Arrange
        Game game = Game.empty();
        doNothing().when(gameRepository).deleteByIdOrThrow(game.getId());

        // Act
        service.deleteGame(game.getId());

        // Assert
        verify(gameRepository).deleteByIdOrThrow(game.getId());
    }

    @Test
    void givenGameDoesNotExist_whenDeleteGame_thenThrowGameNotFoundException() {
        // Arrange
        doThrow(new GameNotFoundException(NONEXISTENT_ID))
                .when(gameRepository).deleteByIdOrThrow(NONEXISTENT_ID);

        // Act & Assert
        assertThatThrownBy(() -> service.deleteGame(NONEXISTENT_ID))
                .isInstanceOf(GameNotFoundException.class)
                .hasMessage(new GameNotFoundException(NONEXISTENT_ID).getMessage());
    }

    @Test
    void givenGameWithDeck_whenShuffleGameDeck_thenNoExceptionThrown() {
        // Arrange
        Game game = Game.empty().withGameDeck(
            Game.empty().getGameDeck().addDeck(Deck.standardDeck())
        );
        when(gameRepository.updateAtomically(eq(game.getId()), any()))
            .thenAnswer(inv -> {
                var updater = (java.util.function.Function<Game, Game>) inv.getArgument(1);
                return Optional.of(updater.apply(game));
            });

        // Act & Assert
        assertThatNoException().isThrownBy(
            () -> service.shuffleGameDeck(game.getId())
        );
    }

    @Test
    void givenNonexistentGameId_whenShuffleGameDeck_thenThrowGameNotFoundException() {
        // Arrange
        when(gameRepository.updateAtomically(eq(NONEXISTENT_ID), any()))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.shuffleGameDeck(NONEXISTENT_ID))
            .isInstanceOf(GameNotFoundException.class)
            .hasMessage(new GameNotFoundException(NONEXISTENT_ID).getMessage());
    }
}
