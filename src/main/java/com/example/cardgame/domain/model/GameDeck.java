package com.example.cardgame.domain.model;

import com.example.cardgame.domain.exception.InvalidGameDeckException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public final class GameDeck {
    private static final String HEARTS_KEY = "hearts";
    private static final String SPADES_KEY = "spades";
    private static final String CLUBS_KEY = "clubs";
    private static final String DIAMONDS_KEY = "diamonds";

    private final List<Card> cards;

    public GameDeck(List<Card> cards) {
        if (cards == null) throw InvalidGameDeckException.nullCards();
        this.cards = Collections.unmodifiableList(new ArrayList<>(cards));
    }

    public static GameDeck empty() {
        return new GameDeck(new ArrayList<>());
    }

    public GameDeck addDeck(Deck deck) {
        if (deck == null) throw InvalidGameDeckException.nullDeck();
        List<Card> combined = new ArrayList<>(this.cards);
        combined.addAll(deck.getCards());
        return new GameDeck(combined);
    }

    public int getRemainingCount() {
        return cards.size();
    }

    public DealResult dealToPlayer(int count) {
        if (count < 0) {
            throw InvalidGameDeckException.negativeCount();
        }
        if (count > cards.size()) {
            return new DealResult(List.of(), this);
        }
        List<Card> dealt = new ArrayList<>(cards.subList(0, count));
        List<Card> remaining = new ArrayList<>(cards.subList(count, cards.size()));
        return new DealResult(dealt, new GameDeck(remaining));
    }

    public GameDeck shuffle(Random random) {
        if (random == null) throw InvalidGameDeckException.nullRandom();
        List<Card> shuffled = new ArrayList<>(cards);
        for (int i = shuffled.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Card temp = shuffled.get(i);
            shuffled.set(i, shuffled.get(j));
            shuffled.set(j, temp);
        }
        return new GameDeck(shuffled);
    }

    public SuitCountResult countBySuit() {
        Map<String, Integer> counts = new HashMap<>();
        counts.put(HEARTS_KEY, 0);
        counts.put(SPADES_KEY, 0);
        counts.put(CLUBS_KEY, 0);
        counts.put(DIAMONDS_KEY, 0);

        for (Card card : cards) {
            String suitKey = switch (card.getSuit()) {
                case HEARTS -> HEARTS_KEY;
                case SPADES -> SPADES_KEY;
                case CLUBS -> CLUBS_KEY;
                case DIAMONDS -> DIAMONDS_KEY;
            };
            counts.put(suitKey, counts.get(suitKey) + 1);
        }

        return new SuitCountResult(counts);
    }

    public DetailedCardsResult detailedRemainingCards() {
        List<Card> sorted = cards.stream()
                .sorted((c1, c2) -> {
                    int suitCmp = c1.getSuit().compareTo(c2.getSuit());
                    if (suitCmp != 0) return suitCmp;
                    return c2.getRank().compareTo(c1.getRank());
                })
                .toList();

        return new DetailedCardsResult(sorted);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameDeck gameDeck)) return false;
        return Objects.equals(cards, gameDeck.cards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cards);
    }
}
