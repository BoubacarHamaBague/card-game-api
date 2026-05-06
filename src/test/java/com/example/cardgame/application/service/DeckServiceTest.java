package com.example.cardgame.application.service;

import com.example.cardgame.domain.exception.DeckNotFoundException;
import com.example.cardgame.domain.exception.GameNotFoundException;
import com.example.cardgame.domain.model.Deck;
import com.example.cardgame.domain.model.Game;
import com.example.cardgame.domain.repository.IDeckRepository;
import com.example.cardgame.domain.repository.IGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class DeckServiceTest {

    private static final String NONEXISTENT_ID = "nonexistent";
    private static final int STANDARD_DECK_SIZE = 52;

    private DeckService service;

    @Mock
    private IGameRepository gameRepository;

    @Mock
    private IDeckRepository deckRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new DeckService(gameRepository, deckRepository);
    }

    @Test
    void givenRepositoryReturnsDeck_whenCreateDeck_thenDeckIsCreatedWithCorrectSize() {
        // Arrange
        when(deckRepository.save(any(Deck.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Deck deck = service.createDeck();

        // Assert
        assertThat(deck).isNotNull();
        assertThat(deck.getSize()).isEqualTo(STANDARD_DECK_SIZE);
    }

    @Test
    void givenGameAndDeckExist_whenAddDeckToGame_thenDeckIsAddedToGame() {
        // Arrange
        Game game = Game.empty();
        Deck deck = Deck.standardDeck();

        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));
        when(deckRepository.findById(deck.getId())).thenReturn(Optional.of(deck));
        when(gameRepository.updateAtomically(eq(game.getId()), any()))
                .thenAnswer(inv -> {
                    var updater = (java.util.function.Function<Game, Game>) inv.getArgument(1);
                    return Optional.of(updater.apply(game));
                });

        // Act
        Game result = service.addDeckToGame(game.getId(), deck.getId());

        // Assert
        assertThat(result.getGameDeck().getRemainingCount()).isEqualTo(STANDARD_DECK_SIZE);
    }

    @Test
    void givenGameDoesNotExist_whenAddDeckToGame_thenThrowGameNotFoundException() {
        // Arrange
        Deck deck = Deck.standardDeck();
        when(gameRepository.findById(NONEXISTENT_ID)).thenReturn(Optional.empty());
        when(deckRepository.findById(deck.getId())).thenReturn(Optional.of(deck));

        // Act & Assert
        assertThatThrownBy(() -> service.addDeckToGame(NONEXISTENT_ID, deck.getId()))
                .isInstanceOf(GameNotFoundException.class)
                .hasMessage(new GameNotFoundException(NONEXISTENT_ID).getMessage());
    }

    @Test
    void givenDeckDoesNotExist_whenAddDeckToGame_thenThrowDeckNotFoundException() {
        // Arrange
        Game game = Game.empty();
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));
        when(deckRepository.findById(NONEXISTENT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.addDeckToGame(game.getId(), NONEXISTENT_ID))
                .isInstanceOf(DeckNotFoundException.class)
                .hasMessage(new DeckNotFoundException(NONEXISTENT_ID).getMessage());
    }
}
