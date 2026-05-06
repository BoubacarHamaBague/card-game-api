package com.example.cardgame.integration;

import com.example.cardgame.application.service.CardDealingService;
import com.example.cardgame.application.service.DeckService;
import com.example.cardgame.application.service.GameService;
import com.example.cardgame.application.service.GameStatisticsService;
import com.example.cardgame.application.service.PlayerService;
import com.example.cardgame.domain.exception.InvalidGameDeckException;
import com.example.cardgame.domain.model.Card;
import com.example.cardgame.domain.model.Player;
import com.example.cardgame.domain.util.ErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class ComprehensiveEdgeCasesTest {

    @Autowired private GameService gameService;
    @Autowired private PlayerService playerService;
    @Autowired private DeckService deckService;
    @Autowired private CardDealingService cardDealingService;
    @Autowired private GameStatisticsService statisticsService;

    private static final int STANDARD_DECK_SIZE = 52;
    private static final int TWO_DECKS_SIZE = 104;
    private static final String PLAYER_ALICE = "Alice";
    private static final String PLAYER_BOB = "Bob";
    private static final String PLAYER_CHARLIE = "Charlie";
    private static final String PLAYER_DAVID = "David";
    private static final String PLAYER_EVE = "Eve";

    private String gameId;
    private String deckId;

    @BeforeEach
    void setUp() {
        var game = gameService.createGame();
        gameId = game.getId();

        var deck = deckService.createDeck();
        deckId = deck.getId();

        deckService.addDeckToGame(gameId, deckId);
    }

    @Test
    void givenGameWithFullDeck_whenDealAll52Cards_thenPlayerHasAllCards() {
        // Arrange
        Player alice = playerService.addPlayer(gameId, PLAYER_ALICE);

        // Act
        List<Card> dealt = cardDealingService.dealCards(gameId, alice.getId(), STANDARD_DECK_SIZE);

        // Assert
        assertThat(dealt).hasSize(STANDARD_DECK_SIZE);
    }

    @Test
    void givenGameWithDeckOf52_whenDeal53Cards_thenNothingDealt() {
        // Arrange
        Player alice = playerService.addPlayer(gameId, PLAYER_ALICE);

        // Act
        List<Card> dealt = cardDealingService.dealCards(gameId, alice.getId(), 53);

        // Assert
        assertThat(dealt).isEmpty();
    }

    @Test
    void givenGameWithTwoPlayers_whenDeal26CardsEach_thenBothReceive26() {
        // Arrange
        Player alice = playerService.addPlayer(gameId, PLAYER_ALICE);
        Player bob = playerService.addPlayer(gameId, PLAYER_BOB);

        // Act
        List<Card> aliceCards = cardDealingService.dealCards(gameId, alice.getId(), 26);
        List<Card> bobCards = cardDealingService.dealCards(gameId, bob.getId(), 26);

        // Assert
        assertThat(aliceCards).hasSize(26);
        assertThat(bobCards).hasSize(26);
    }

    @Test
    void givenGameWithTwoPlayers_whenDeal27CardsToFirst_thenRemaining25Available() {
        // Arrange
        Player alice = playerService.addPlayer(gameId, PLAYER_ALICE);
        Player bob = playerService.addPlayer(gameId, PLAYER_BOB);

        // Act
        List<Card> aliceCards = cardDealingService.dealCards(gameId, alice.getId(), 27);
        List<Card> bobCards = cardDealingService.dealCards(gameId, bob.getId(), 25);

        // Assert
        assertThat(aliceCards).hasSize(27);
        assertThat(bobCards).hasSize(25);
    }

    @Test
    void givenGame_whenDeal0Cards_thenNothingHappens() {
        // Arrange
        Player alice = playerService.addPlayer(gameId, PLAYER_ALICE);

        // Act
        List<Card> dealt = cardDealingService.dealCards(gameId, alice.getId(), 0);

        // Assert
        assertThat(dealt).isEmpty();
    }

    @Test
    void givenGameWithFivePlayers_whenDeal10CardsEach_thenAll50Dealt() {
        // Arrange
        Player alice = playerService.addPlayer(gameId, PLAYER_ALICE);
        Player bob = playerService.addPlayer(gameId, PLAYER_BOB);
        Player charlie = playerService.addPlayer(gameId, PLAYER_CHARLIE);
        Player david = playerService.addPlayer(gameId, PLAYER_DAVID);
        Player eve = playerService.addPlayer(gameId, PLAYER_EVE);

        // Act
        List<Card> aliceCards = cardDealingService.dealCards(gameId, alice.getId(), 10);
        List<Card> bobCards = cardDealingService.dealCards(gameId, bob.getId(), 10);
        List<Card> charlieCards = cardDealingService.dealCards(gameId, charlie.getId(), 10);
        List<Card> davidCards = cardDealingService.dealCards(gameId, david.getId(), 10);
        List<Card> eveCards = cardDealingService.dealCards(gameId, eve.getId(), 10);

        // Assert
        assertThat(aliceCards).hasSize(10);
        assertThat(bobCards).hasSize(10);
        assertThat(charlieCards).hasSize(10);
        assertThat(davidCards).hasSize(10);
        assertThat(eveCards).hasSize(10);
    }

    @Test
    void givenGameWithTwoDecks_whenDeal104Cards_thenAllDealt() {
        // Arrange
        var deck2 = deckService.createDeck();
        deckService.addDeckToGame(gameId, deck2.getId());

        Player alice = playerService.addPlayer(gameId, PLAYER_ALICE);

        // Act
        List<Card> dealt = cardDealingService.dealCards(gameId, alice.getId(), TWO_DECKS_SIZE);

        // Assert
        assertThat(dealt).hasSize(TWO_DECKS_SIZE);
    }

    @Test
    void givenGameWithTwoDecks_whenDeal105Cards_thenNothingDealt() {
        // Arrange
        var deck2 = deckService.createDeck();
        deckService.addDeckToGame(gameId, deck2.getId());

        Player alice = playerService.addPlayer(gameId, PLAYER_ALICE);

        // Act
        List<Card> dealt = cardDealingService.dealCards(gameId, alice.getId(), 105);

        // Assert - All-or-nothing: 105 > 104, so deal 0
        assertThat(dealt).isEmpty();
    }

    @Test
    void givenDeckWith52_whenDeal52Then1_thenSecondDealFails() {
        // Arrange
        Player alice = playerService.addPlayer(gameId, PLAYER_ALICE);

        // Act
        List<Card> first = cardDealingService.dealCards(gameId, alice.getId(), 52);
        List<Card> second = cardDealingService.dealCards(gameId, alice.getId(), 1);

        // Assert
        assertThat(first).hasSize(52);
        assertThat(second).isEmpty();
    }

    @Test
    void givenFivePlayers_whenDealAlternating_thenExactly52Total() {
        // Arrange
        Player alice = playerService.addPlayer(gameId, PLAYER_ALICE);
        Player bob = playerService.addPlayer(gameId, PLAYER_BOB);
        Player charlie = playerService.addPlayer(gameId, PLAYER_CHARLIE);
        Player david = playerService.addPlayer(gameId, PLAYER_DAVID);
        Player eve = playerService.addPlayer(gameId, PLAYER_EVE);

        // Act - Deal exact amount (52 total)
        List<Card> a1 = cardDealingService.dealCards(gameId, alice.getId(), 10);
        List<Card> b1 = cardDealingService.dealCards(gameId, bob.getId(), 10);
        List<Card> c1 = cardDealingService.dealCards(gameId, charlie.getId(), 10);
        List<Card> d1 = cardDealingService.dealCards(gameId, david.getId(), 10);
        List<Card> e1 = cardDealingService.dealCards(gameId, eve.getId(), 12); // 10+10+10+10+12=52

        // Assert
        int totalDealt = a1.size() + b1.size() + c1.size() + d1.size() + e1.size();
        assertThat(totalDealt).isEqualTo(52);
    }

    @Test
    void givenGame_whenDealNegativeCards_thenThrowException() {
        // Arrange
        Player alice = playerService.addPlayer(gameId, PLAYER_ALICE);

        // Act & Assert
        assertThatThrownBy(() -> cardDealingService.dealCards(gameId, alice.getId(), -5))
                .isInstanceOf(InvalidGameDeckException.class)
                .hasMessage(ErrorMessage.INVALID_COUNT);
    }

    @Test
    void givenGame_whenDealVeryLargeNumber_thenNothingDealt() {
        // Arrange
        Player alice = playerService.addPlayer(gameId, PLAYER_ALICE);

        // Act
        List<Card> dealt = cardDealingService.dealCards(gameId, alice.getId(), 999999);

        // Assert
        assertThat(dealt).isEmpty();
    }

    @Test
    void givenEmptyGame_whenGetStatistics_thenAllCardsRemain() {
        // Act
        var suits = statisticsService.getRemainingSuitCount(gameId);

        // Assert
        assertThat(suits).isNotEmpty();
    }

    @Test
    void givenThreePlayers_whenRemoveOne_thenTwoRemain() {
        // Arrange
        Player alice = playerService.addPlayer(gameId, PLAYER_ALICE);
        Player bob = playerService.addPlayer(gameId, PLAYER_BOB);
        Player charlie = playerService.addPlayer(gameId, PLAYER_CHARLIE);

        // Act
        playerService.removePlayer(gameId, bob.getId());
        cardDealingService.dealCards(gameId, alice.getId(), 10);
        cardDealingService.dealCards(gameId, charlie.getId(), 10);

        // Assert
        assertThat(playerService.getPlayer(gameId, alice.getId())).isNotNull();
        assertThat(playerService.getPlayer(gameId, charlie.getId())).isNotNull();
    }

    @Test
    void givenDeck_whenDealCards_thenRemainingCountDecreases() {
        // Arrange
        Player alice = playerService.addPlayer(gameId, PLAYER_ALICE);
        var beforeCards = statisticsService.getRemainingCardsDetail(gameId);
        int beforeCount = beforeCards.size();

        // Act
        cardDealingService.dealCards(gameId, alice.getId(), 10);
        var afterCards = statisticsService.getRemainingCardsDetail(gameId);
        int afterCount = afterCards.size();

        // Assert
        assertThat(afterCount).isEqualTo(beforeCount - 10);
    }

    @Test
    void givenTwoDecksAndFivePlayers_whenDeal20Each_then100Dealt() {
        // Arrange
        var deck2 = deckService.createDeck();
        deckService.addDeckToGame(gameId, deck2.getId());

        Player alice = playerService.addPlayer(gameId, PLAYER_ALICE);
        Player bob = playerService.addPlayer(gameId, PLAYER_BOB);
        Player charlie = playerService.addPlayer(gameId, PLAYER_CHARLIE);
        Player david = playerService.addPlayer(gameId, PLAYER_DAVID);
        Player eve = playerService.addPlayer(gameId, PLAYER_EVE);

        // Act
        List<Card> a = cardDealingService.dealCards(gameId, alice.getId(), 20);
        List<Card> b = cardDealingService.dealCards(gameId, bob.getId(), 20);
        List<Card> c = cardDealingService.dealCards(gameId, charlie.getId(), 20);
        List<Card> d = cardDealingService.dealCards(gameId, david.getId(), 20);
        List<Card> e = cardDealingService.dealCards(gameId, eve.getId(), 20);

        // Assert
        int total = a.size() + b.size() + c.size() + d.size() + e.size();
        assertThat(total).isEqualTo(100);
    }

    @Test
    void givenGameAndNullDeck_whenAddDeck_thenThrowException() {
        // Act & Assert
        assertThatThrownBy(() -> deckService.addDeckToGame(gameId, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void givenGameWithDeck_whenAddSameDeckAgain_thenGameHas104Cards() {
        // Arrange
        deckService.addDeckToGame(gameId, deckId);

        // Act
        Player alice = playerService.addPlayer(gameId, PLAYER_ALICE);
        List<Card> dealt = cardDealingService.dealCards(gameId, alice.getId(), 104);

        // Assert
        assertThat(dealt).hasSize(104);
    }

    @Test
    void givenGameWithPartialDeal_whenDealExactRemaining_thenSuccess() {
        // Arrange
        Player alice = playerService.addPlayer(gameId, PLAYER_ALICE);
        Player bob = playerService.addPlayer(gameId, PLAYER_BOB);

        cardDealingService.dealCards(gameId, alice.getId(), 30);

        // Act
        List<Card> bobCards = cardDealingService.dealCards(gameId, bob.getId(), 22);

        // Assert
        assertThat(bobCards).hasSize(22);
    }

    @Test
    void givenGameWithRemaining22_whenDeal21Then1_thenSecondSucceeds() {
        // Arrange
        Player alice = playerService.addPlayer(gameId, PLAYER_ALICE);
        Player bob = playerService.addPlayer(gameId, PLAYER_BOB);

        cardDealingService.dealCards(gameId, alice.getId(), 30);

        // Act
        List<Card> bob1 = cardDealingService.dealCards(gameId, bob.getId(), 21);
        List<Card> bob2 = cardDealingService.dealCards(gameId, bob.getId(), 1);

        // Assert
        assertThat(bob1).hasSize(21);
        assertThat(bob2).hasSize(1);
    }

    @Test
    void givenGame_whenDealRapidly5Times_thenAllSucceed() {
        // Arrange
        Player alice = playerService.addPlayer(gameId, PLAYER_ALICE);

        // Act & Assert
        assertThat(cardDealingService.dealCards(gameId, alice.getId(), 10)).hasSize(10);
        assertThat(cardDealingService.dealCards(gameId, alice.getId(), 8)).hasSize(8);
        assertThat(cardDealingService.dealCards(gameId, alice.getId(), 12)).hasSize(12);
        assertThat(cardDealingService.dealCards(gameId, alice.getId(), 15)).hasSize(15);
        assertThat(cardDealingService.dealCards(gameId, alice.getId(), 7)).hasSize(7);
    }

    @Test
    void givenGame_whenDeal51ThenNeed2_thenSecondFails() {
        // Arrange
        Player alice = playerService.addPlayer(gameId, PLAYER_ALICE);

        // Act & Assert
        assertThat(cardDealingService.dealCards(gameId, alice.getId(), 51)).hasSize(51);
        assertThat(cardDealingService.dealCards(gameId, alice.getId(), 2)).isEmpty();
    }

    @Test
    void givenFivePlayers_whenDealDifferentAmounts_thenTotalLess52() {
        // Arrange
        Player alice = playerService.addPlayer(gameId, PLAYER_ALICE);
        Player bob = playerService.addPlayer(gameId, PLAYER_BOB);
        Player charlie = playerService.addPlayer(gameId, PLAYER_CHARLIE);
        Player david = playerService.addPlayer(gameId, PLAYER_DAVID);
        Player eve = playerService.addPlayer(gameId, PLAYER_EVE);

        // Act
        List<Card> a = cardDealingService.dealCards(gameId, alice.getId(), 5);
        List<Card> b = cardDealingService.dealCards(gameId, bob.getId(), 8);
        List<Card> c = cardDealingService.dealCards(gameId, charlie.getId(), 3);
        List<Card> d = cardDealingService.dealCards(gameId, david.getId(), 12);
        List<Card> e = cardDealingService.dealCards(gameId, eve.getId(), 10);

        // Assert
        int total = a.size() + b.size() + c.size() + d.size() + e.size();
        assertThat(total).isLessThan(52).isEqualTo(38);
    }

    @Test
    void givenSinglePlayer_whenGetAll52ThenTryMore_thenSecondEmpty() {
        // Arrange
        Player alice = playerService.addPlayer(gameId, PLAYER_ALICE);

        // Act
        List<Card> all = cardDealingService.dealCards(gameId, alice.getId(), 52);
        List<Card> more = cardDealingService.dealCards(gameId, alice.getId(), 1);

        // Assert
        assertThat(all).hasSize(52);
        assertThat(more).isEmpty();
    }

    @Test
    void givenFivePlayers_whenDealInterleaved_thenAllSucceed() {
        // Arrange
        Player alice = playerService.addPlayer(gameId, PLAYER_ALICE);
        Player bob = playerService.addPlayer(gameId, PLAYER_BOB);
        Player charlie = playerService.addPlayer(gameId, PLAYER_CHARLIE);
        Player david = playerService.addPlayer(gameId, PLAYER_DAVID);
        Player eve = playerService.addPlayer(gameId, PLAYER_EVE);

        // Act & Assert
        assertThat(cardDealingService.dealCards(gameId, alice.getId(), 5)).hasSize(5);
        assertThat(cardDealingService.dealCards(gameId, bob.getId(), 5)).hasSize(5);
        assertThat(cardDealingService.dealCards(gameId, charlie.getId(), 5)).hasSize(5);
        assertThat(cardDealingService.dealCards(gameId, david.getId(), 5)).hasSize(5);
        assertThat(cardDealingService.dealCards(gameId, eve.getId(), 5)).hasSize(5);

        assertThat(cardDealingService.dealCards(gameId, alice.getId(), 5)).hasSize(5);
        assertThat(cardDealingService.dealCards(gameId, bob.getId(), 5)).hasSize(5);
        assertThat(cardDealingService.dealCards(gameId, charlie.getId(), 5)).hasSize(5);
        assertThat(cardDealingService.dealCards(gameId, david.getId(), 5)).hasSize(5);
        assertThat(cardDealingService.dealCards(gameId, eve.getId(), 2)).hasSize(2);
    }
}
