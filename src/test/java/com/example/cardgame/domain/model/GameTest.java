package com.example.cardgame.domain.model;

import com.example.cardgame.domain.exception.InvalidGameException;
import com.example.cardgame.domain.exception.PlayerNotFoundException;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GameTest {

    private static final String PLAYER_NAME_ALICE = "Alice";
    private static final String PLAYER_NAME_BOB = "Bob";
    private static final String PLAYER_ID_TEST = "test-id";
    private static final String NONEXISTENT_ID = "nonexistent";
    private static final int ONE_PLAYER_SIZE = 1;
    private static final int TWO_PLAYERS_SIZE = 2;
    private static final int EMPTY_REMAINING_CARDS = 0;
    private static final int STANDARD_DECK_CARDS = 52;
    private static final int ALICE_HAND_VALUE = 25;
    private static final int BOB_HAND_VALUE = 1;

    @Test
    void givenEmptyGame_whenCreate_thenGameHasIdAndNoPlayersOrCards() {
        // Arrange & Act
        Game game = Game.empty();

        // Assert
        assertThat(game.getId()).isNotNull();
        assertThat(game.getPlayers()).isEmpty();
        assertThat(game.getGameDeck().getRemainingCount()).isEqualTo(EMPTY_REMAINING_CARDS);
    }

    @Test
    void givenEmptyGame_whenAddPlayer_thenGameRetainsImmutabilityAndPlayerIsAdded() {
        // Arrange
        Game game = Game.empty();
        Player player = new Player(PLAYER_NAME_ALICE);

        // Act
        Game updated = game.addPlayer(player);

        // Assert
        assertThat(game.getPlayers()).isEmpty();
        assertThat(updated.getPlayers()).hasSize(ONE_PLAYER_SIZE);
        assertThat(updated.hasPlayer(player.getId())).isTrue();
    }

    @Test
    void givenGameWithPlayer_whenAddDuplicatePlayer_thenThrowInvalidGameException() {
        // Arrange
        Game game = Game.empty();
        Player player = new Player(PLAYER_ID_TEST, PLAYER_NAME_ALICE, List.of());

        // Act & Assert
        Game afterFirstAdd = game.addPlayer(player);
        assertThatThrownBy(() -> afterFirstAdd.addPlayer(player))
                .isInstanceOf(InvalidGameException.class)
                .hasMessage("Player already exists: " + PLAYER_ID_TEST);
    }

    @Test
    void givenGameWithPlayer_whenRemovePlayer_thenPlayerIsRemoved() {
        // Arrange
        Game game = Game.empty();
        Player player = new Player(PLAYER_NAME_ALICE);
        Game withPlayer = game.addPlayer(player);

        // Act
        Game updated = withPlayer.removePlayer(player.getId());

        // Assert
        assertThat(withPlayer.getPlayers()).hasSize(ONE_PLAYER_SIZE);
        assertThat(updated.getPlayers()).isEmpty();
    }

    @Test
    void givenEmptyGame_whenRemoveNonexistentPlayer_thenThrowPlayerNotFoundException() {
        // Arrange
        Game game = Game.empty();

        // Act & Assert
        assertThatThrownBy(() -> game.removePlayer(NONEXISTENT_ID))
                .isInstanceOf(PlayerNotFoundException.class)
                .hasMessage("Player not found: " + NONEXISTENT_ID);
    }

    @Test
    void givenGameAndGameDeck_whenSetGameDeck_thenGameRetainsImmutabilityAndDeckIsUpdated() {
        // Arrange
        Game game = Game.empty();
        GameDeck deck = GameDeck.empty().addDeck(Deck.standardDeck());

        // Act
        Game updated = game.withGameDeck(deck);

        // Assert
        assertThat(game.getGameDeck().getRemainingCount()).isEqualTo(EMPTY_REMAINING_CARDS);
        assertThat(updated.getGameDeck().getRemainingCount()).isEqualTo(STANDARD_DECK_CARDS);
    }

    @Test
    void givenGameWithPlayersHavingCards_whenGetPlayersSortedByHandValue_thenReturnSortedByDescendingValue() {
        // Arrange
        Game game = Game.empty();
        Player alice = new Player(PLAYER_NAME_ALICE);
        Player bob = new Player(PLAYER_NAME_BOB);

        Game withPlayers = game.addPlayer(alice).addPlayer(bob);

        List<Card> aliceCards = List.of(new Card(Suit.HEARTS, Rank.KING),
                new Card(Suit.HEARTS, Rank.QUEEN));
        List<Card> bobCards = List.of(new Card(Suit.HEARTS, Rank.ACE));

        Player aliceWithCards = alice.withAdditionalCards(aliceCards);
        Player bobWithCards = bob.withAdditionalCards(bobCards);

        // Act
        Game withCards = withPlayers.withPlayer(aliceWithCards).withPlayer(bobWithCards);
        List<Player> sorted = withCards.getPlayersSortedByHandValueDesc();

        // Assert
        assertThat(sorted).hasSize(TWO_PLAYERS_SIZE);
        assertThat(sorted.get(0).getHandValue()).isEqualTo(ALICE_HAND_VALUE);
        assertThat(sorted.get(1).getHandValue()).isEqualTo(BOB_HAND_VALUE);
    }

    @Test
    void givenGame_whenAddPlayerToOriginal_thenOriginalGameRemainsImmutable() {
        // Arrange
        Game game1 = Game.empty();
        Player player = new Player(PLAYER_NAME_ALICE);

        // Act
        Game game2 = game1.addPlayer(player);

        // Assert
        assertThat(game1.getPlayers()).isEmpty();
        assertThat(game2.getPlayers()).hasSize(ONE_PLAYER_SIZE);
    }

}
