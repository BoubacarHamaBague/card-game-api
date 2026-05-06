package com.example.cardgame.domain.model;

import com.example.cardgame.domain.exception.InvalidCardException;
import com.example.cardgame.domain.util.ErrorMessage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CardTest {

    private static final int ACE_VALUE = 1;
    private static final int TWO_VALUE = 2;
    private static final int TEN_VALUE = 10;
    private static final int JACK_VALUE = 11;
    private static final int QUEEN_VALUE = 12;
    private static final int KING_VALUE = 13;
    private static final String KING_ABBREVIATION = "K";
    private static final String HEARTS_STRING = "Hearts";

    @Test
    void givenHeartsSuitAndAceRank_whenCardCreation_thenCardHasCorrectProperties() {
        // Arrange
        Suit suit = Suit.HEARTS;
        Rank rank = Rank.ACE;

        // Act
        Card card = new Card(suit, rank);

        // Assert
        assertThat(card.getSuit()).isEqualTo(Suit.HEARTS);
        assertThat(card.getRank()).isEqualTo(Rank.ACE);
        assertThat(card.getValue()).isEqualTo(ACE_VALUE);
    }

    @Test
    void givenVariousRanks_whenCreateCards_thenCardValuesAreCorrect() {
        // Arrange & Act & Assert
        assertThat(new Card(Suit.HEARTS, Rank.ACE).getValue()).isEqualTo(ACE_VALUE);
        assertThat(new Card(Suit.HEARTS, Rank.TWO).getValue()).isEqualTo(TWO_VALUE);
        assertThat(new Card(Suit.HEARTS, Rank.TEN).getValue()).isEqualTo(TEN_VALUE);
        assertThat(new Card(Suit.HEARTS, Rank.JACK).getValue()).isEqualTo(JACK_VALUE);
        assertThat(new Card(Suit.HEARTS, Rank.QUEEN).getValue()).isEqualTo(QUEEN_VALUE);
        assertThat(new Card(Suit.HEARTS, Rank.KING).getValue()).isEqualTo(KING_VALUE);
    }

    @Test
    void givenSameSuitAndRank_whenComparingCards_thenCardsAreEqual() {
        // Arrange
        Card card1 = new Card(Suit.HEARTS, Rank.ACE);
        Card card2 = new Card(Suit.HEARTS, Rank.ACE);
        Card card3 = new Card(Suit.SPADES, Rank.ACE);

        // Act & Assert
        assertThat(card1).isEqualTo(card2);
        assertThat(card1).isNotEqualTo(card3);
    }

    @Test
    void givenNullSuit_whenCreateCard_thenThrowInvalidCardException() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Card(null, Rank.ACE))
                .isInstanceOf(InvalidCardException.class)
                .hasMessage(ErrorMessage.SUIT_NULL);
    }

    @Test
    void givenNullRank_whenCreateCard_thenThrowInvalidCardException() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> new Card(Suit.HEARTS, null))
                .isInstanceOf(InvalidCardException.class)
                .hasMessage(ErrorMessage.RANK_NULL);
    }

    @Test
    void givenKingOfHearts_whenConvertToString_thenStringContainsKAndHearts() {
        // Arrange
        Card card = new Card(Suit.HEARTS, Rank.KING);

        // Act & Assert
        assertThat(card.toString()).contains(KING_ABBREVIATION, HEARTS_STRING);
    }

    @Test
    void givenTwoEqualCards_whenComputeHashCode_thenHashCodesAreEqual() {
        // Arrange
        Card card1 = new Card(Suit.HEARTS, Rank.KING);
        Card card2 = new Card(Suit.HEARTS, Rank.KING);

        // Act & Assert
        assertThat(card1.hashCode()).isEqualTo(card2.hashCode());
    }

    @Test
    void givenCard_whenCompareWithItself_thenEqual() {
        // Arrange
        Card card = new Card(Suit.SPADES, Rank.QUEEN);

        // Act & Assert
        assertThat(card).isEqualTo(card);
    }

    @Test
    void givenCard_whenCompareWithNonCard_thenNotEqual() {
        // Arrange
        Card card = new Card(Suit.HEARTS, Rank.KING);
        String notACard = "not a card";

        // Act & Assert
        assertThat(card).isNotEqualTo(notACard);
    }

    @Test
    void givenCard_whenCompareWithNull_thenNotEqual() {
        // Arrange
        Card card = new Card(Suit.HEARTS, Rank.KING);

        // Act & Assert
        assertThat(card).isNotEqualTo(null);
    }
}
