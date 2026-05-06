package com.example.cardgame.domain.model;

import com.example.cardgame.domain.exception.InvalidDeckException;
import com.example.cardgame.domain.util.ErrorMessage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class DeckTest {

    private static final int STANDARD_DECK_SIZE = 52;
    private static final int CARDS_PER_SUIT = 13;
    private static final int CARDS_PER_RANK = 4;
    private static final String DECK_ID = "id";

    @Test
    void givenStandardDeck_whenCreateDeck_thenDeckHas52Cards() {
        // Arrange & Act
        Deck deck = Deck.standardDeck();

        // Assert
        assertThat(deck.getSize()).isEqualTo(STANDARD_DECK_SIZE);
    }

    @Test
    void givenStandardDeck_whenCountCardsPerSuit_thenEachSuitHas13Cards() {
        // Arrange
        Deck deck = Deck.standardDeck();

        // Act & Assert
        for (Suit suit : Suit.values()) {
            long count = deck.getCards().stream()
                    .filter(card -> card.getSuit() == suit)
                    .count();
            assertThat(count).isEqualTo(CARDS_PER_SUIT);
        }
    }

    @Test
    void givenStandardDeck_whenCountCardsPerRank_thenEachRankHas4Cards() {
        // Arrange
        Deck deck = Deck.standardDeck();

        // Act & Assert
        for (Rank rank : Rank.values()) {
            long count = deck.getCards().stream()
                    .filter(card -> card.getRank() == rank)
                    .count();
            assertThat(count).isEqualTo(CARDS_PER_RANK);
        }
    }

    @Test
    void givenStandardDeck_whenTryToModifyCardsList_thenThrowUnsupportedOperationException() {
        // Arrange
        Deck deck = Deck.standardDeck();

        // Act & Assert
        assertThatThrownBy(() -> deck.getCards().add(new Card(Suit.HEARTS, Rank.ACE)))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void givenTwoNewDecks_whenCompareIds_thenIdsDiffer() {
        // Arrange & Act
        Deck deck1 = Deck.standardDeck();
        Deck deck2 = Deck.standardDeck();

        // Assert
        assertThat(deck1.getId()).isNotEqualTo(deck2.getId());
    }

    @Test
    void givenNullCards_whenCreateDeck_thenThrowInvalidDeckException() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Deck(DECK_ID, null))
                .isInstanceOf(InvalidDeckException.class)
                .hasMessage(ErrorMessage.CARDS_NULL);
    }
}
