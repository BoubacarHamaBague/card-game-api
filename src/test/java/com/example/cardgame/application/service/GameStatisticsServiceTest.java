package com.example.cardgame.application.service;

import com.example.cardgame.domain.exception.GameNotFoundException;
import com.example.cardgame.domain.model.Card;
import com.example.cardgame.domain.model.Deck;
import com.example.cardgame.domain.model.Game;
import com.example.cardgame.domain.model.Player;
import com.example.cardgame.domain.model.Rank;
import com.example.cardgame.domain.model.Suit;
import com.example.cardgame.domain.repository.IGameRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameStatisticsServiceTest {

    private static final String NONEXISTENT_ID = "nonexistent";
    private static final String HEARTS = "hearts";
    private static final int CARDS_PER_SUIT = 13;
    private static final int STANDARD_DECK_SIZE = 52;

    private GameStatisticsService service;

    @Mock
    private IGameRepository gameRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new GameStatisticsService(gameRepository);
    }

    @Test
    void givenEmptyGame_whenGetPlayersRanking_thenReturnEmptyRanking() {
        // Arrange
        Game game = Game.empty();
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        // Act
        var ranking = service.getPlayersRanking(game.getId());

        // Assert
        assertThat(ranking).isEmpty();
    }

    @Test
    void givenGameWithPlayersHavingCards_whenGetPlayersRanking_thenReturnSortedByHandValue() {
        // Arrange
        Player alice = new Player("Alice").withAdditionalCards(
                java.util.List.of(
                        new Card(Suit.HEARTS, Rank.KING),
                        new Card(Suit.HEARTS, Rank.QUEEN)
                )
        );
        Player bob = new Player("Bob").withAdditionalCards(
                java.util.List.of(
                        new Card(Suit.SPADES, Rank.ACE),
                        new Card(Suit.SPADES, Rank.TEN)
                )
        );
        Player charlie = new Player("Charlie").withAdditionalCards(
                java.util.List.of(
                        new Card(Suit.CLUBS, Rank.KING),
                        new Card(Suit.CLUBS, Rank.JACK)
                )
        );

        Game game = Game.empty().withPlayer(alice).withPlayer(bob).withPlayer(charlie);
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        // Act
        var ranking = service.getPlayersRanking(game.getId());

        // Assert
        assertThat(ranking).hasSize(3);
        assertThat(ranking.get(0).getName()).isEqualTo("Alice");
        assertThat(ranking.get(0).getHandValue()).isEqualTo(25);
        assertThat(ranking.get(1).getName()).isEqualTo("Charlie");
        assertThat(ranking.get(1).getHandValue()).isEqualTo(24);
        assertThat(ranking.get(2).getName()).isEqualTo("Bob");
        assertThat(ranking.get(2).getHandValue()).isEqualTo(11);
    }

    @Test
    void givenGameDoesNotExist_whenGetPlayersRanking_thenThrowGameNotFoundException() {
        // Arrange
        when(gameRepository.findById(NONEXISTENT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.getPlayersRanking(NONEXISTENT_ID))
                .isInstanceOf(GameNotFoundException.class)
                .hasMessage(new GameNotFoundException(NONEXISTENT_ID).getMessage());
    }

    @Test
    void givenGameWithStandardDeck_whenGetRemainingSuitCount_thenReturnCorrectCounts() {
        // Arrange
        Game game = Game.empty().withGameDeck(
                Game.empty().getGameDeck().addDeck(Deck.standardDeck())
        );
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        // Act
        var counts = service.getRemainingSuitCount(game.getId());

        // Assert
        assertThat(counts).containsEntry(HEARTS, CARDS_PER_SUIT);
    }

    @Test
    void givenGameDoesNotExist_whenGetRemainingSuitCount_thenThrowGameNotFoundException() {
        // Arrange
        when(gameRepository.findById(NONEXISTENT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.getRemainingSuitCount(NONEXISTENT_ID))
                .isInstanceOf(GameNotFoundException.class)
                .hasMessage(new GameNotFoundException(NONEXISTENT_ID).getMessage());
    }

    @Test
    void givenGameWithStandardDeck_whenGetRemainingCardsDetail_thenReturn52Cards() {
        // Arrange
        Game game = Game.empty().withGameDeck(
                Game.empty().getGameDeck().addDeck(Deck.standardDeck())
        );
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        // Act
        var cards = service.getRemainingCardsDetail(game.getId());

        // Assert
        assertThat(cards).hasSize(STANDARD_DECK_SIZE);
    }

    @Test
    void givenGameDoesNotExist_whenGetRemainingCardsDetail_thenThrowGameNotFoundException() {
        // Arrange
        when(gameRepository.findById(NONEXISTENT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.getRemainingCardsDetail(NONEXISTENT_ID))
                .isInstanceOf(GameNotFoundException.class)
                .hasMessage(new GameNotFoundException(NONEXISTENT_ID).getMessage());
    }
}
