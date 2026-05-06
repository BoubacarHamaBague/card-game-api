package com.example.cardgame.domain.model;

import org.junit.jupiter.api.Test;
import java.util.Random;

import static org.assertj.core.api.Assertions.*;

class GameDeckTest {

    private static final int STANDARD_DECK_SIZE = 52;
    private static final int TWO_DECKS_SIZE = 104;
    private static final int CARDS_TO_DEAL = 5;
    private static final int REMAINING_AFTER_DEAL = 47;
    private static final int EXCESS_CARDS_TO_DEAL = 100;
    private static final int CARDS_PER_SUIT = 13;
    private static final String HEARTS = "hearts";
    private static final String SPADES = "spades";
    private static final String CLUBS = "clubs";
    private static final String DIAMONDS = "diamonds";

    @Test
    void givenEmptyGameDeck_whenAddDeck_thenGameDeckRetainsImmutabilityAndDeckIsAdded() {
        // Arrange
        GameDeck gameDeck = GameDeck.empty();
        Deck deck = Deck.standardDeck();

        // Act
        GameDeck updated = gameDeck.addDeck(deck);

        // Assert
        assertThat(gameDeck.getRemainingCount()).isEqualTo(0);
        assertThat(updated.getRemainingCount()).isEqualTo(STANDARD_DECK_SIZE);
    }

    @Test
    void givenEmptyGameDeck_whenAddTwoDecks_thenGameDeckHas104Cards() {
        // Arrange
        GameDeck gameDeck = GameDeck.empty();
        Deck deck1 = Deck.standardDeck();
        Deck deck2 = Deck.standardDeck();

        // Act
        GameDeck updated = gameDeck.addDeck(deck1).addDeck(deck2);

        // Assert
        assertThat(updated.getRemainingCount()).isEqualTo(TWO_DECKS_SIZE);
    }

    @Test
    void givenGameDeckWithDeck_whenDealCardToPlayer_thenCardsAreDealtAndRemainingCountUpdates() {
        // Arrange
        GameDeck gameDeck = GameDeck.empty().addDeck(Deck.standardDeck());

        // Act
        DealResult result = gameDeck.dealToPlayer(CARDS_TO_DEAL);

        // Assert
        assertThat(result.dealt()).hasSize(CARDS_TO_DEAL);
        assertThat(result.remaining().getRemainingCount()).isEqualTo(REMAINING_AFTER_DEAL);
    }

    @Test
    void givenGameDeckWithDeck_whenDealMoreCardsThanAvailable_thenNothingIsDealedAndDeckUnchanged() {
        // Arrange
        GameDeck gameDeck = GameDeck.empty().addDeck(Deck.standardDeck());

        // Act
        DealResult result = gameDeck.dealToPlayer(EXCESS_CARDS_TO_DEAL);

        // Assert
        assertThat(result.dealt()).isEmpty();
        assertThat(result.remaining().getRemainingCount()).isEqualTo(STANDARD_DECK_SIZE);
    }

    @Test
    void givenGameDeckWithDeck_whenShuffle_thenOriginalAndShuffledAreImmutableAndDifferent() {
        // Arrange
        GameDeck gameDeck = GameDeck.empty().addDeck(Deck.standardDeck());

        // Act
        GameDeck shuffled = gameDeck.shuffle(new Random());

        // Assert
        assertThat(gameDeck.getRemainingCount()).isEqualTo(STANDARD_DECK_SIZE);
        assertThat(shuffled.getRemainingCount()).isEqualTo(STANDARD_DECK_SIZE);
        assertThat(gameDeck).isNotEqualTo(shuffled);
    }

    @Test
    void givenGameDeckWithStandardDeck_whenCountBySuit_thenEachSuitHas13Cards() {
        // Arrange
        GameDeck gameDeck = GameDeck.empty().addDeck(Deck.standardDeck());

        // Act
        SuitCountResult result = gameDeck.countBySuit();

        // Assert
        assertThat(result.counts().get(HEARTS)).isEqualTo(CARDS_PER_SUIT);
        assertThat(result.counts().get(SPADES)).isEqualTo(CARDS_PER_SUIT);
        assertThat(result.counts().get(CLUBS)).isEqualTo(CARDS_PER_SUIT);
        assertThat(result.counts().get(DIAMONDS)).isEqualTo(CARDS_PER_SUIT);
    }

    @Test
    void givenGameDeckWithDeckAfterDeal_whenCountBySuit_thenSuitCountIsReduced() {
        // Arrange
        GameDeck gameDeck = GameDeck.empty().addDeck(Deck.standardDeck());
        DealResult dealResult = gameDeck.dealToPlayer(CARDS_TO_DEAL);

        // Act
        SuitCountResult counts = dealResult.remaining().countBySuit();

        // Assert
        int total = counts.counts().values().stream().mapToInt(Integer::intValue).sum();
        assertThat(total).isEqualTo(REMAINING_AFTER_DEAL);
    }

    @Test
    void givenGameDeckWithStandardDeck_whenGetDetailedRemainingCards_thenReturnAll52Cards() {
        // Arrange
        GameDeck gameDeck = GameDeck.empty().addDeck(Deck.standardDeck());

        // Act
        DetailedCardsResult result = gameDeck.detailedRemainingCards();

        // Assert
        assertThat(result.cards()).hasSize(STANDARD_DECK_SIZE);
    }

    @Test
    void givenGameDeckWithDeck_whenDealCards_thenOriginalGameDeckRemainsMutable() {
        // Arrange
        GameDeck gameDeck = GameDeck.empty().addDeck(Deck.standardDeck());

        // Act
        DealResult result = gameDeck.dealToPlayer(CARDS_TO_DEAL);

        // Assert
        assertThat(gameDeck.getRemainingCount()).isEqualTo(STANDARD_DECK_SIZE);
        assertThat(result.remaining().getRemainingCount()).isEqualTo(REMAINING_AFTER_DEAL);
    }

    @Test
    void givenGameDeckWithStandardDeck_whenGetDetailedCards_thenFirstCardIsKingOfHeartsAndLastIsAceOfDiamonds() {
        // Arrange
        GameDeck gameDeck = GameDeck.empty().addDeck(Deck.standardDeck());

        // Act
        DetailedCardsResult result = gameDeck.detailedRemainingCards();

        // Assert
        assertThat(result.cards().get(0).getSuit()).isEqualTo(Suit.HEARTS);
        assertThat(result.cards().get(0).getRank()).isEqualTo(Rank.KING);
        assertThat(result.cards().get(51).getSuit()).isEqualTo(Suit.DIAMONDS);
        assertThat(result.cards().get(51).getRank()).isEqualTo(Rank.ACE);
    }

    @Test
    void givenGameDeckWithStandardDeck_whenGetDetailedCards_thenCardsAreSortedByGoalOrder() {
        // Arrange
        GameDeck gameDeck = GameDeck.empty().addDeck(Deck.standardDeck());

        // Act
        DetailedCardsResult result = gameDeck.detailedRemainingCards();

        // Assert
        assertThat(result.cards().get(0).getSuit()).isEqualTo(Suit.HEARTS);
        assertThat(result.cards().get(13).getSuit()).isEqualTo(Suit.SPADES);
        assertThat(result.cards().get(26).getSuit()).isEqualTo(Suit.CLUBS);
        assertThat(result.cards().get(39).getSuit()).isEqualTo(Suit.DIAMONDS);

        assertThat(result.cards().get(0).getRank()).isEqualTo(Rank.KING);
        assertThat(result.cards().get(1).getRank()).isEqualTo(Rank.QUEEN);
        assertThat(result.cards().get(2).getRank()).isEqualTo(Rank.JACK);
        assertThat(result.cards().get(12).getRank()).isEqualTo(Rank.ACE);
    }

    @Test
    void givenGameDeckWithStandardDeck_when52ConsecutiveDeals_thenAllCardsAreDealtAnd53rdDealGivesNothing() {
        // Arrange
        GameDeck gameDeck = GameDeck.empty().addDeck(Deck.standardDeck());
        int cardsDealtTotal = 0;

        // Act
        GameDeck remaining = gameDeck;
        for (int i = 0; i < STANDARD_DECK_SIZE; i++) {
            DealResult result = remaining.dealToPlayer(1);
            assertThat(result.dealt()).hasSize(1);
            cardsDealtTotal++;
            remaining = result.remaining();
        }

        // Assert
        assertThat(cardsDealtTotal).isEqualTo(STANDARD_DECK_SIZE);
        assertThat(remaining.getRemainingCount()).isEqualTo(0);

        DealResult result53 = remaining.dealToPlayer(1);
        assertThat(result53.dealt()).isEmpty();
        assertThat(result53.remaining().getRemainingCount()).isEqualTo(0);
    }
}
