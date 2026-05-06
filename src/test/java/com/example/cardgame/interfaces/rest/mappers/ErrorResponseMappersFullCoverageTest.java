package com.example.cardgame.interfaces.rest.mappers;

import com.example.cardgame.domain.exception.DeckNotFoundException;
import com.example.cardgame.domain.exception.GameNotFoundException;
import com.example.cardgame.domain.exception.PlayerNotFoundException;
import com.example.cardgame.interfaces.rest.dto.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class ErrorResponseMappersFullCoverageTest {

    @Autowired
    private GameNotFoundExceptionMapper gameNotFoundMapper;

    @Autowired
    private PlayerNotFoundExceptionMapper playerNotFoundMapper;

    @Autowired
    private DeckNotFoundExceptionMapper deckNotFoundMapper;

    @Autowired
    private CatchallExceptionMapper catchallMapper;

    @Test
    void givenGameNotFoundException_whenHandle_thenReturn404WithErrorResponse() {
        // Arrange
        GameNotFoundException exception = new GameNotFoundException("game-123");

        // Act
        ResponseEntity<ErrorResponse> response = gameNotFoundMapper.handle(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).contains("game-123");
    }

    @Test
    void givenPlayerNotFoundException_whenHandle_thenReturn404WithErrorResponse() {
        // Arrange
        PlayerNotFoundException exception = new PlayerNotFoundException("player-456");

        // Act
        ResponseEntity<ErrorResponse> response = playerNotFoundMapper.handle(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).contains("player-456");
    }

    @Test
    void givenDeckNotFoundException_whenHandle_thenReturn404WithErrorResponse() {
        // Arrange
        DeckNotFoundException exception = new DeckNotFoundException("deck-789");

        // Act
        ResponseEntity<ErrorResponse> response = deckNotFoundMapper.handle(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).contains("deck-789");
    }

    @Test
    void givenCatchallException_whenHandle_thenReturn500WithErrorResponse() {
        // Arrange
        Exception exception = new Exception("Unexpected error");

        // Act
        ResponseEntity<ErrorResponse> response = catchallMapper.handle(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).isNotBlank();
    }

    @Test
    void givenGameNotFoundMapperRespondsWithCorrectStatusCode() {
        // Arrange
        GameNotFoundException exception = new GameNotFoundException("test");

        // Act
        ResponseEntity<ErrorResponse> response = gameNotFoundMapper.handle(exception);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void givenPlayerNotFoundMapperRespondsWithCorrectStatusCode() {
        // Arrange
        PlayerNotFoundException exception = new PlayerNotFoundException("test");

        // Act
        ResponseEntity<ErrorResponse> response = playerNotFoundMapper.handle(exception);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void givenDeckNotFoundMapperRespondsWithCorrectStatusCode() {
        // Arrange
        DeckNotFoundException exception = new DeckNotFoundException("test");

        // Act
        ResponseEntity<ErrorResponse> response = deckNotFoundMapper.handle(exception);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    void givenCatchallMapperRespondsWithCorrectStatusCode() {
        // Arrange
        Exception exception = new Exception("test");

        // Act
        ResponseEntity<ErrorResponse> response = catchallMapper.handle(exception);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(500);
    }

    @Test
    void givenMultipleExceptionInstances_whenMapperHandles_thenConsistentBehavior() {
        // Arrange
        GameNotFoundException exception1 = new GameNotFoundException("id1");
        GameNotFoundException exception2 = new GameNotFoundException("id2");

        // Act
        ResponseEntity<ErrorResponse> response1 = gameNotFoundMapper.handle(exception1);
        ResponseEntity<ErrorResponse> response2 = gameNotFoundMapper.handle(exception2);

        // Assert
        assertThat(response1.getStatusCode()).isEqualTo(response2.getStatusCode());
        assertThat(response1.getBody()).isNotNull();
        assertThat(response2.getBody()).isNotNull();
    }
}
