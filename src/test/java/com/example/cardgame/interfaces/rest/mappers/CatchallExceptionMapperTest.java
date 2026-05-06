package com.example.cardgame.interfaces.rest.mappers;

import com.example.cardgame.interfaces.rest.dto.ErrorResponse;
import com.fasterxml.jackson.core.JsonParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class CatchallExceptionMapperTest {

    @Autowired
    private CatchallExceptionMapper mapper;

    @Test
    void givenGenericException_whenHandle_thenReturn500WithGenericMessage() {
        // Arrange
        Exception exception = new Exception("Some error");

        // Act
        ResponseEntity<ErrorResponse> response = mapper.handle(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).isEqualTo("Internal server error");
    }

    @Test
    void givenGenericException_whenHandle_thenErrorCodeIsNull() {
        // Arrange
        Exception exception = new Exception("Some error");

        // Act
        ResponseEntity<ErrorResponse> response = mapper.handle(exception);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(500);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().errorCode()).isNull();
    }

    @Test
    void givenJsonParseException_whenHandle_thenReturn400WithInvalidJsonMessage() {
        // Arrange
        JsonParseException exception = new JsonParseException("Invalid JSON", (com.fasterxml.jackson.core.JsonLocation) null);

        // Act
        ResponseEntity<ErrorResponse> response = mapper.handleJsonParseException(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).isEqualTo("Invalid JSON format");
    }

    @Test
    void givenJsonParseException_whenHandle_thenErrorCodeIsNull() {
        // Arrange
        JsonParseException exception = new JsonParseException("Invalid JSON", (com.fasterxml.jackson.core.JsonLocation) null);

        // Act
        ResponseEntity<ErrorResponse> response = mapper.handleJsonParseException(exception);

        // Assert
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().errorCode()).isNull();
    }
}
