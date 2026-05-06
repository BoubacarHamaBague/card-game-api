package com.example.cardgame.infrastructure.persistence.memory;

import com.example.cardgame.domain.exception.GameNotFoundException;
import com.example.cardgame.domain.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class InMemoryGameRepositoryTest {

    private static final String NONEXISTENT_ID = "nonexistent";
    private static final String PLAYER_NAME_ALICE = "Alice";
    private static final String PLAYER_NAME_BOB = "Bob";
    private static final String PLAYER_NAME_CHARLIE = "Charlie";
    private static final String PLAYER_NAME_DAVID = "David";
    private static final int ONE_PLAYER_SIZE = 1;
    private static final int FOUR_PLAYERS_SIZE = 4;

    private InMemoryGameRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryGameRepository();
    }

    @Test
    void givenGame_whenSaveAndFind_thenGameIsRetrieved() {
        // Arrange
        Game game = Game.empty();

        // Act
        Game saved = repository.save(game);

        // Assert
        assertThat(saved.getId()).isEqualTo(game.getId());
        assertThat(repository.findById(game.getId())).contains(game);
    }

    @Test
    void givenNonexistentGameId_whenFind_thenReturnEmpty() {
        // Arrange & Act & Assert
        assertThat(repository.findById(NONEXISTENT_ID)).isEmpty();
    }

    @Test
    void givenSavedGame_whenDelete_thenGameIsRemoved() {
        // Arrange
        Game game = Game.empty();
        repository.save(game);

        // Act
        repository.deleteByIdOrThrow(game.getId());

        // Assert
        assertThat(repository.findById(game.getId())).isEmpty();
    }

    @Test
    void givenNonexistentGameId_whenDeleteByIdOrThrow_thenThrowGameNotFoundException() {
        // Arrange & Act & Assert
        assertThatThrownBy(() -> repository.deleteByIdOrThrow(NONEXISTENT_ID))
                .isInstanceOf(GameNotFoundException.class);
    }

    @Test
    void givenSavedGame_whenUpdateWithPlayer_thenGameIsUpdated() {
        // Arrange
        Game game1 = Game.empty();
        Game saved1 = repository.save(game1);

        // Act
        Game game2 = saved1.addPlayer(new com.example.cardgame.domain.model.Player(PLAYER_NAME_ALICE));
        Game saved2 = repository.save(game2);

        // Assert
        assertThat(repository.findById(game1.getId()).get().getPlayers()).hasSize(ONE_PLAYER_SIZE);
    }

    @Test
    void givenRepository_whenSaveTwoGamesInConcurrentThreads_thenBothGamesAreSaved() throws InterruptedException {
        // Arrange
        Game game1 = Game.empty();
        Game game2 = Game.empty();

        // Act
        Thread t1 = new Thread(() -> repository.save(game1));
        Thread t2 = new Thread(() -> repository.save(game2));

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // Assert
        assertThat(repository.findById(game1.getId())).isPresent();
        assertThat(repository.findById(game2.getId())).isPresent();
    }

    @Test
    void givenGameWithPlayersAndConcurrentUpdates_whenUpdateAtomically_thenAllUpdatesAreApplied() throws InterruptedException {
        // Arrange
        Game initialGame = Game.empty()
                .addPlayer(new com.example.cardgame.domain.model.Player(PLAYER_NAME_ALICE))
                .addPlayer(new com.example.cardgame.domain.model.Player(PLAYER_NAME_BOB));
        repository.save(initialGame);

        // Act
        Thread t1 = new Thread(() -> {
            var result = repository.updateAtomically(initialGame.getId(), game ->
                    game.addPlayer(new com.example.cardgame.domain.model.Player(PLAYER_NAME_CHARLIE)));
            assertThat(result).isPresent();
        });

        Thread t2 = new Thread(() -> {
            var result = repository.updateAtomically(initialGame.getId(), game ->
                    game.addPlayer(new com.example.cardgame.domain.model.Player(PLAYER_NAME_DAVID)));
            assertThat(result).isPresent();
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // Assert
        Game finalGame = repository.findById(initialGame.getId()).get();
        assertThat(finalGame.getPlayers()).hasSize(FOUR_PLAYERS_SIZE);
    }

    @Test
    void givenNonexistentGameIdAndUpdateFunction_whenUpdateAtomically_thenReturnEmpty() {
        // Arrange & Act
        var result = repository.updateAtomically(NONEXISTENT_ID, game -> game);

        // Assert
        assertThat(result).isEmpty();
    }
}
