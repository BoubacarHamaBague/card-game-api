package com.example.cardgame.infrastructure.persistence.memory;

import com.example.cardgame.domain.model.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class InMemoryDeckRepositoryTest {

    private static final String NONEXISTENT_ID = "nonexistent";

    private InMemoryDeckRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryDeckRepository();
    }

    @Test
    void givenDeck_whenSaveAndFind_thenDeckIsRetrieved() {
        // Arrange
        Deck deck = Deck.standardDeck();

        // Act
        Deck saved = repository.save(deck);

        // Assert
        assertThat(saved.getId()).isEqualTo(deck.getId());
        assertThat(repository.findById(deck.getId())).contains(deck);
    }

    @Test
    void givenNonexistentDeckId_whenFind_thenReturnEmpty() {
        // Arrange & Act & Assert
        assertThat(repository.findById(NONEXISTENT_ID)).isEmpty();
    }

    @Test
    void givenSavedDeck_whenDelete_thenDeckIsRemoved() {
        // Arrange
        Deck deck = Deck.standardDeck();
        repository.save(deck);

        // Act
        repository.deleteByIdOrThrow(deck.getId());

        // Assert
        assertThat(repository.findById(deck.getId())).isEmpty();
    }

    @Test
    void givenTwoDecks_whenSaveMultipleDecks_thenBothDecksAreRetrievable() {
        // Arrange
        Deck deck1 = Deck.standardDeck();
        Deck deck2 = Deck.standardDeck();

        // Act
        repository.save(deck1);
        repository.save(deck2);

        // Assert
        assertThat(repository.findById(deck1.getId())).isPresent();
        assertThat(repository.findById(deck2.getId())).isPresent();
    }

    @Test
    void givenRepository_whenSaveTwoDecksInConcurrentThreads_thenBothDecksAreSaved() throws InterruptedException {
        // Arrange
        Deck deck1 = Deck.standardDeck();
        Deck deck2 = Deck.standardDeck();

        // Act
        Thread t1 = new Thread(() -> repository.save(deck1));
        Thread t2 = new Thread(() -> repository.save(deck2));

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // Assert
        assertThat(repository.findById(deck1.getId())).isPresent();
        assertThat(repository.findById(deck2.getId())).isPresent();
    }
}
