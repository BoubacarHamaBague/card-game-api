package com.example.cardgame.application.service;

import com.example.cardgame.domain.exception.GameNotFoundException;
import com.example.cardgame.domain.exception.PlayerNotFoundException;
import com.example.cardgame.domain.model.Game;
import com.example.cardgame.domain.model.Player;
import com.example.cardgame.domain.repository.IGameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PlayerServiceTest {

    private static final String PLAYER_NAME_ALICE = "Alice";
    private static final String NONEXISTENT_ID = "nonexistent";
    private static final String GAME_ID = "gameId";
    private static final String PLAYER_ID = "playerId";

    private PlayerService service;

    @Mock
    private IGameRepository gameRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new PlayerService(gameRepository);
    }

    @Test
    void givenGameIdAndPlayerName_whenAddPlayer_thenPlayerIsAddedToGame() {
        // Arrange
        Game game = Game.empty();
        when(gameRepository.updateAtomically(eq(game.getId()), any()))
                .thenAnswer(inv -> {
                    var updater = (java.util.function.Function<Game, Game>) inv.getArgument(1);
                    return Optional.of(updater.apply(game));
                });

        // Act
        Player result = service.addPlayer(game.getId(), PLAYER_NAME_ALICE);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(PLAYER_NAME_ALICE);
    }

    @Test
    void givenNonexistentGameId_whenAddPlayer_thenThrowGameNotFoundException() {
        // Arrange
        when(gameRepository.updateAtomically(eq(NONEXISTENT_ID), any()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.addPlayer(NONEXISTENT_ID, PLAYER_NAME_ALICE))
                .isInstanceOf(GameNotFoundException.class)
                .hasMessage(new GameNotFoundException(NONEXISTENT_ID).getMessage());
    }

    @Test
    void givenGameWithPlayerAndPlayerId_whenRemovePlayer_thenPlayerIsRemoved() {
        // Arrange
        Game game = Game.empty();
        Player player = new Player(PLAYER_NAME_ALICE);
        Game gameWithPlayer = game.addPlayer(player);

        when(gameRepository.updateAtomically(eq(game.getId()), any()))
                .thenAnswer(inv -> {
                    var updater = (java.util.function.Function<Game, Game>) inv.getArgument(1);
                    return Optional.of(updater.apply(gameWithPlayer));
                });

        // Act
        service.removePlayer(game.getId(), player.getId());

        // Assert
        verify(gameRepository).updateAtomically(eq(game.getId()), any());
    }

    @Test
    void givenNonexistentGameId_whenRemovePlayer_thenOrElseThrowRaisesGameNotFoundException() {
        // Arrange
        when(gameRepository.updateAtomically(eq(NONEXISTENT_ID), any()))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.removePlayer(NONEXISTENT_ID, PLAYER_ID))
                .isInstanceOf(GameNotFoundException.class)
                .hasMessage(new GameNotFoundException(NONEXISTENT_ID).getMessage());
    }

    @Test
    void givenGameWithPlayerAndInvalidPlayerId_whenRemovePlayer_thenThrowPlayerNotFoundException() {
        // Arrange
        when(gameRepository.updateAtomically(eq(GAME_ID), any()))
                .thenThrow(new PlayerNotFoundException(PLAYER_ID));

        // Act & Assert
        assertThatThrownBy(() -> service.removePlayer(GAME_ID, PLAYER_ID))
                .isInstanceOf(PlayerNotFoundException.class)
                .hasMessage(new PlayerNotFoundException(PLAYER_ID).getMessage());
    }

    @Test
    void givenGameWithPlayer_whenGetPlayer_thenReturnPlayer() {
        // Arrange
        Game game = Game.empty();
        Player player = new Player(PLAYER_NAME_ALICE);
        Game gameWithPlayer = game.addPlayer(player);

        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(gameWithPlayer));

        // Act
        Player result = service.getPlayer(game.getId(), player.getId());

        // Assert
        assertThat(result.getName()).isEqualTo(PLAYER_NAME_ALICE);
    }

    @Test
    void givenNonexistentGameId_whenGetPlayer_thenThrowGameNotFoundException() {
        // Arrange
        when(gameRepository.findById(NONEXISTENT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.getPlayer(NONEXISTENT_ID, PLAYER_ID))
                .isInstanceOf(GameNotFoundException.class)
                .hasMessage(new GameNotFoundException(NONEXISTENT_ID).getMessage());
    }

    @Test
    void givenGameWithoutPlayerAndInvalidPlayerId_whenGetPlayer_thenThrowPlayerNotFoundException() {
        // Arrange
        Game game = Game.empty();
        when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

        // Act & Assert
        assertThatThrownBy(() -> service.getPlayer(game.getId(), NONEXISTENT_ID))
                .isInstanceOf(PlayerNotFoundException.class)
                .hasMessage(new PlayerNotFoundException(NONEXISTENT_ID).getMessage());
    }
}
