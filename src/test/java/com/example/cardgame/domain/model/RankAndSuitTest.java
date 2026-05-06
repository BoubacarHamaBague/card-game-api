package com.example.cardgame.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RankAndSuitTest {

    @Test
    void givenAllRanks_whenIterateValues_thenAllPresent() {
        // Arrange & Act & Assert
        assertThat(Rank.values()).hasSize(13);
        assertThat(Rank.values()).contains(
                Rank.ACE, Rank.TWO, Rank.THREE, Rank.FOUR, Rank.FIVE,
                Rank.SIX, Rank.SEVEN, Rank.EIGHT, Rank.NINE, Rank.TEN,
                Rank.JACK, Rank.QUEEN, Rank.KING
        );
    }

    @Test
    void givenAceRank_whenGetValue_thenValue1() {
        assertThat(Rank.ACE.getValue()).isEqualTo(1);
    }

    @Test
    void givenTwoRank_whenGetValue_thenValue2() {
        assertThat(Rank.TWO.getValue()).isEqualTo(2);
    }

    @Test
    void givenTenRank_whenGetValue_thenValue10() {
        assertThat(Rank.TEN.getValue()).isEqualTo(10);
    }

    @Test
    void givenJackRank_whenGetValue_thenValue11() {
        assertThat(Rank.JACK.getValue()).isEqualTo(11);
    }

    @Test
    void givenQueenRank_whenGetValue_thenValue12() {
        assertThat(Rank.QUEEN.getValue()).isEqualTo(12);
    }

    @Test
    void givenKingRank_whenGetValue_thenValue13() {
        assertThat(Rank.KING.getValue()).isEqualTo(13);
    }

    @Test
    void givenAllRanksEnum_whenGetAll_thenCanEnumerateAll() {
        // Arrange & Act
        int count = 0;
        for (Rank rank : Rank.values()) {
            assertThat(rank).isNotNull();
            assertThat(rank.getValue()).isPositive();
            count++;
        }

        // Assert
        assertThat(count).isEqualTo(13);
    }

    @Test
    void givenAllSuits_whenIterateValues_thenAllPresent() {
        // Arrange & Act & Assert
        assertThat(Suit.values()).hasSize(4);
        assertThat(Suit.values()).contains(
                Suit.HEARTS, Suit.SPADES, Suit.CLUBS, Suit.DIAMONDS
        );
    }

    @Test
    void givenHeartsSuit_whenCompare_thenCanCompare() {
        // Arrange & Act & Assert
        assertThat(Suit.HEARTS).isNotEqualTo(Suit.SPADES);
        assertThat(Suit.HEARTS).isEqualTo(Suit.HEARTS);
    }

    @Test
    void givenSpadesSuit_whenCompare_thenCanCompare() {
        // Arrange & Act & Assert
        assertThat(Suit.SPADES).isNotEqualTo(Suit.CLUBS);
        assertThat(Suit.SPADES).isEqualTo(Suit.SPADES);
    }

    @Test
    void givenClubsSuit_whenCompare_thenCanCompare() {
        // Arrange & Act & Assert
        assertThat(Suit.CLUBS).isNotEqualTo(Suit.DIAMONDS);
        assertThat(Suit.CLUBS).isEqualTo(Suit.CLUBS);
    }

    @Test
    void givenDiamondsSuit_whenCompare_thenCanCompare() {
        // Arrange & Act & Assert
        assertThat(Suit.DIAMONDS).isNotEqualTo(Suit.HEARTS);
        assertThat(Suit.DIAMONDS).isEqualTo(Suit.DIAMONDS);
    }

    @Test
    void givenRankAndSuit_whenGetNames_thenNamesAreValid() {
        // Arrange & Act & Assert
        for (Rank rank : Rank.values()) {
            assertThat(rank.name()).isNotBlank();
        }

        for (Suit suit : Suit.values()) {
            assertThat(suit.name()).isNotBlank();
        }
    }

    @Test
    void givenRankValueOf_whenConvert_thenConversionWorks() {
        // Arrange & Act & Assert
        assertThat(Rank.valueOf("ACE")).isEqualTo(Rank.ACE);
        assertThat(Rank.valueOf("KING")).isEqualTo(Rank.KING);
    }

    @Test
    void givenSuitValueOf_whenConvert_thenConversionWorks() {
        // Arrange & Act & Assert
        assertThat(Suit.valueOf("HEARTS")).isEqualTo(Suit.HEARTS);
        assertThat(Suit.valueOf("DIAMONDS")).isEqualTo(Suit.DIAMONDS);
    }
}
