package com.example.cardgame.domain.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.*;

class DomainModelsEdgeCasesTest {

    @Test
    void givenTwoPlayersWithSameId_whenCompareEquality_thenEqual() {
        // Arrange
        String playerId = "player-123";
        Player p1 = new Player(playerId, "Alice", new java.util.ArrayList<>());
        Player p2 = new Player(playerId, "Bob", new java.util.ArrayList<>());

        // Act & Assert
        assertThat(p1).isEqualTo(p2);
    }

    @Test
    void givenPlayer_whenGetHashCode_thenConsistent() {
        // Arrange
        Player p1 = new Player("player-1", "Alice", new java.util.ArrayList<>());
        Player p2 = new Player("player-1", "Alice", new java.util.ArrayList<>());

        // Act & Assert
        assertThat(p1.hashCode()).isEqualTo(p2.hashCode());
    }

    @Test
    void givenTwoDecksWithSameId_whenCompareEquality_thenEqual() {
        // Arrange
        String deckId = "deck-123";
        Deck d1 = new Deck(deckId, new java.util.ArrayList<>());
        Deck d2 = new Deck(deckId, new java.util.ArrayList<>());

        // Act & Assert
        assertThat(d1).isEqualTo(d2);
    }

    @Test
    void givenDeck_whenGetHashCode_thenConsistent() {
        // Arrange
        Deck d1 = new Deck("deck-1", new java.util.ArrayList<>());
        Deck d2 = new Deck("deck-1", new java.util.ArrayList<>());

        // Act & Assert
        assertThat(d1.hashCode()).isEqualTo(d2.hashCode());
    }

    @Test
    void givenGame_whenCompareWithSelf_thenEqual() {
        // Arrange
        Game game = Game.empty();

        // Act & Assert
        assertThat(game).isEqualTo(game);
    }

    @Test
    void givenGame_whenGetHashCode_thenConsistent() {
        // Arrange
        Game game1 = Game.empty();
        String id = game1.getId();
        Game game2 = new Game(id, GameDeck.empty(), new HashMap<>());

        // Act & Assert
        assertThat(game1.hashCode()).isEqualTo(game2.hashCode());
    }

    @Test
    void givenPlayer_whenToString_thenContainsIdAndName() {
        // Arrange
        Player player = new Player("id-123", "Alice", new java.util.ArrayList<>());

        // Act
        String str = player.toString();

        // Assert
        assertThat(str).contains("id-123", "Alice");
    }

    @Test
    void givenDeck_whenToString_thenStringIsGenerated() {
        // Arrange
        Deck deck = new Deck("deck-456", new java.util.ArrayList<>());

        // Act
        String str = deck.toString();

        // Assert
        assertThat(str).isNotBlank().contains("Deck");
    }

    @Test
    void givenGame_whenToString_thenContainsId() {
        // Arrange
        Game game = Game.empty();

        // Act
        String str = game.toString();

        // Assert
        assertThat(str).contains(game.getId());
    }

    @Test
    void givenPlayer_whenCompareNonPlayerObject_thenNotEqual() {
        // Arrange
        Player player = new Player("id-1", "Alice", new java.util.ArrayList<>());
        String notAPlayer = "not a player";

        // Act & Assert
        assertThat(player).isNotEqualTo(notAPlayer);
    }

    @Test
    void givenDeck_whenCompareNonDeckObject_thenNotEqual() {
        // Arrange
        Deck deck = new Deck("id-1", new java.util.ArrayList<>());
        String notADeck = "not a deck";

        // Act & Assert
        assertThat(deck).isNotEqualTo(notADeck);
    }

    @Test
    void givenGame_whenCompareNonGameObject_thenNotEqual() {
        // Arrange
        Game game = Game.empty();
        String notAGame = "not a game";

        // Act & Assert
        assertThat(game).isNotEqualTo(notAGame);
    }

    @Test
    void givenPlayer_whenCompareWithNull_thenNotEqual() {
        // Arrange
        Player player = new Player("id-1", "Alice", new java.util.ArrayList<>());

        // Act & Assert
        assertThat(player).isNotEqualTo(null);
    }

    @Test
    void givenDeck_whenCompareWithNull_thenNotEqual() {
        // Arrange
        Deck deck = new Deck("id-1", new java.util.ArrayList<>());

        // Act & Assert
        assertThat(deck).isNotEqualTo(null);
    }

    @Test
    void givenGame_whenCompareWithNull_thenNotEqual() {
        // Arrange
        Game game = Game.empty();

        // Act & Assert
        assertThat(game).isNotEqualTo(null);
    }

    @Test
    void givenGameDeck_whenCompareEquality_thenComparisonWorks() {
        // Arrange
        GameDeck deck1 = GameDeck.empty();
        GameDeck deck2 = GameDeck.empty();

        // Act & Assert
        assertThat(deck1).isEqualTo(deck2);
    }

    @Test
    void givenGameDeck_whenGetHashCode_thenConsistent() {
        // Arrange
        GameDeck deck1 = GameDeck.empty();
        GameDeck deck2 = GameDeck.empty();

        // Act & Assert
        assertThat(deck1.hashCode()).isEqualTo(deck2.hashCode());
    }
}
