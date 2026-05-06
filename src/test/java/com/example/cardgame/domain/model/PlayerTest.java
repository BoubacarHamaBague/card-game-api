package com.example.cardgame.domain.model;

import com.example.cardgame.domain.exception.InvalidPlayerException;
import com.example.cardgame.domain.util.ErrorMessage;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PlayerTest {

    private static final String PLAYER_NAME_ALICE = "Alice";
    private static final String PLAYER_NAME_BOB = "Bob";
    private static final String PLAYER_NAME_CHARLIE = "Charlie";
    private static final String PLAYER_NAME_DAVID = "David";
    private static final String PLAYER_ID_TEST = "test-id";
    private static final int EMPTY_HAND_SIZE = 0;
    private static final int EMPTY_HAND_VALUE = 0;
    private static final int ONE_CARD_SIZE = 1;
    private static final int TWO_CARDS_SIZE = 2;
    private static final int THREE_CARDS_SIZE = 3;
    private static final int TWO_CARDS_HAND_VALUE = 25;
    private static final int THREE_CARDS_HAND_VALUE = 6;

    @Test
    void givenNewPlayer_whenCreated_thenHasNameAndEmptyHand() {
        // Arrange
        String name = PLAYER_NAME_ALICE;

        // Act
        Player player = new Player(name);

        // Assert
        assertThat(player.getName()).isEqualTo(PLAYER_NAME_ALICE);
        assertThat(player.getHand()).hasSize(EMPTY_HAND_SIZE);
        assertThat(player.getHandValue()).isEqualTo(EMPTY_HAND_VALUE);
    }

    @Test
    void givenSameNamePlayers_whenCreated_thenHaveDifferentIds() {
        // Arrange & Act
        Player player1 = new Player(PLAYER_NAME_ALICE);
        Player player2 = new Player(PLAYER_NAME_ALICE);

        // Assert
        assertThat(player1.getId()).isNotEqualTo(player2.getId());
    }

    @Test
    void givenPlayerWithNoCards_whenAddingCards_thenUpdatesHandAndValue() {
        // Arrange
        Player player = new Player(PLAYER_NAME_BOB);
        List<Card> newCards = List.of(
                new Card(Suit.HEARTS, Rank.KING),
                new Card(Suit.SPADES, Rank.QUEEN)
        );

        // Act
        Player updated = player.withAdditionalCards(newCards);

        // Assert
        assertThat(player.getHand()).isEmpty();
        assertThat(updated.getHand()).hasSize(TWO_CARDS_SIZE);
        assertThat(updated.getHandValue()).isEqualTo(TWO_CARDS_HAND_VALUE);
    }

    @Test
    void givenPlayerWithCards_whenCalculatingHandValue_thenReturnsCorrectSum() {
        // Arrange
        Player player = new Player(PLAYER_NAME_CHARLIE);
        List<Card> cards = List.of(
                new Card(Suit.HEARTS, Rank.ACE),
                new Card(Suit.HEARTS, Rank.TWO),
                new Card(Suit.HEARTS, Rank.THREE)
        );

        // Act
        Player updated = player.withAdditionalCards(cards);

        // Assert
        assertThat(updated.getHand()).hasSize(THREE_CARDS_SIZE);
        assertThat(updated.getHandValue()).isEqualTo(THREE_CARDS_HAND_VALUE);
    }

    @Test
    void givenOriginalPlayer_whenAddingSameCardsMultipleTimes_thenOriginalUnchanged() {
        // Arrange
        Player player = new Player(PLAYER_NAME_DAVID);
        List<Card> newCards = List.of(new Card(Suit.HEARTS, Rank.KING));

        // Act
        Player updated1 = player.withAdditionalCards(newCards);
        Player updated2 = player.withAdditionalCards(newCards);

        // Assert
        assertThat(player.getHand()).isEmpty();
        assertThat(updated1.getHand()).hasSize(ONE_CARD_SIZE);
        assertThat(updated2.getHand()).hasSize(ONE_CARD_SIZE);
        assertThat(updated1.getId()).isEqualTo(updated2.getId());
    }

    @Test
    void givenNullName_whenCreatingPlayer_thenThrowsInvalidPlayerException() {
        // Act & Assert
        assertThatThrownBy(() -> new Player(null))
                .isInstanceOf(InvalidPlayerException.class)
                .hasMessage(ErrorMessage.PLAYER_NAME_NULL);
    }

    @Test
    void givenTwoPlayersWithSameIdAndName_whenComparing_thenAreEqual() {
        // Arrange
        String id = PLAYER_ID_TEST;
        Player player1 = new Player(id, PLAYER_NAME_ALICE, List.of());
        Player player2 = new Player(id, PLAYER_NAME_ALICE, List.of());

        // Act & Assert
        assertThat(player1).isEqualTo(player2);
    }

    @Test
    void givenTwoPlayersWithSameIdButDifferentName_whenComparing_thenAreEqual() {
        // Arrange
        String id = PLAYER_ID_TEST;
        Player player1 = new Player(id, PLAYER_NAME_ALICE, List.of());
        Player player2 = new Player(id, PLAYER_NAME_BOB, List.of());

        // Act & Assert
        assertThat(player1).isEqualTo(player2);
    }
}
