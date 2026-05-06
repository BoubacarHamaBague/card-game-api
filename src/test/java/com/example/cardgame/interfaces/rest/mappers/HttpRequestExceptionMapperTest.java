package com.example.cardgame.interfaces.rest.mappers;

import com.example.cardgame.interfaces.rest.dto.ErrorResponse;
import com.fasterxml.jackson.core.JsonParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class HttpRequestExceptionMapperTest {

    @Autowired
    private HttpRequestExceptionMapper mapper;

    @Test
    void givenMissingServletRequestParameterException_whenHandle_thenReturn400WithParameterMessage() {
        // Arrange
        MissingServletRequestParameterException exception = new MissingServletRequestParameterException("count", "int");

        // Act
        ResponseEntity<ErrorResponse> response = mapper.handleHttpRequestException(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).contains("count");
    }

    @Test
    void givenHttpMediaTypeNotSupportedException_whenHandle_thenReturn415() {
        // Arrange
        HttpMediaTypeNotSupportedException exception = new HttpMediaTypeNotSupportedException("text/plain");

        // Act
        ResponseEntity<ErrorResponse> response = mapper.handleHttpRequestException(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).contains("Unsupported media type");
    }

    @Test
    void givenJsonParseException_whenHandle_thenReturn400WithJsonMessage() {
        // Arrange
        JsonParseException exception = new JsonParseException("Invalid JSON", (com.fasterxml.jackson.core.JsonLocation) null);

        // Act
        ResponseEntity<ErrorResponse> response = mapper.handleHttpRequestException(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).contains("JSON");
    }

    @Test
    void givenHttpMessageNotReadableException_whenHandle_thenReturn400() {
        // Arrange
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Failed to read message");

        // Act
        ResponseEntity<ErrorResponse> response = mapper.handleHttpRequestException(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void givenHttpMessageNotReadableExceptionWithJsonInMessage_whenHandle_thenReturn400() {
        // Arrange
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("JSON parse error");

        // Act
        ResponseEntity<ErrorResponse> response = mapper.handleHttpRequestException(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).contains("JSON");
    }

    @Test
    void givenHttpMessageNotReadableExceptionWithDuplicateFieldInMessage_whenHandle_thenReturn400() {
        // Arrange
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("duplicate field error");

        // Act
        ResponseEntity<ErrorResponse> response = mapper.handleHttpRequestException(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).contains("JSON");
    }
}
