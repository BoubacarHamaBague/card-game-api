package com.example.cardgame.interfaces.rest.mappers;

import com.example.cardgame.domain.exception.InvalidCardException;
import com.example.cardgame.domain.exception.InvalidDeckException;
import com.example.cardgame.domain.exception.InvalidGameDeckException;
import com.example.cardgame.domain.exception.InvalidGameException;
import com.example.cardgame.domain.exception.InvalidPlayerException;
import com.example.cardgame.interfaces.rest.dto.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
class InvalidParameterExceptionMapperTest {

    @Autowired
    private InvalidParameterExceptionMapper mapper;

    @Test
    void givenInvalidCardException_whenHandle_thenReturn400WithErrorCode() {
        // Arrange
        InvalidCardException exception = InvalidCardException.nullSuit();

        // Act
        ResponseEntity<ErrorResponse> response = mapper.handle(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).isNotBlank();
    }

    @Test
    void givenInvalidPlayerException_whenHandle_thenReturn400WithErrorCode() {
        // Arrange
        InvalidPlayerException exception = InvalidPlayerException.nullName();

        // Act
        ResponseEntity<ErrorResponse> response = mapper.handle(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).isNotBlank();
    }

    @Test
    void givenInvalidDeckException_whenHandle_thenReturn400WithErrorCode() {
        // Arrange
        InvalidDeckException exception = InvalidDeckException.nullId();

        // Act
        ResponseEntity<ErrorResponse> response = mapper.handle(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).isNotBlank();
    }

    @Test
    void givenInvalidGameDeckException_whenHandle_thenReturn400WithErrorCode() {
        // Arrange
        InvalidGameDeckException exception = InvalidGameDeckException.nullCards();

        // Act
        ResponseEntity<ErrorResponse> response = mapper.handle(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).isNotBlank();
    }

    @Test
    void givenInvalidGameException_whenHandle_thenReturn400WithErrorCode() {
        // Arrange
        InvalidGameException exception = InvalidGameException.nullId();

        // Act
        ResponseEntity<ErrorResponse> response = mapper.handle(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).isNotBlank();
    }

    @Test
    void givenIllegalArgumentException_whenHandle_thenReturn400() {
        // Arrange
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        // Act
        ResponseEntity<ErrorResponse> response = mapper.handle(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).contains("Invalid argument");
    }

    @Test
    void givenMethodArgumentNotValidException_whenHandle_thenReturn400WithFieldErrors() {
        // Arrange
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("testObject", "testField", "Test message");
        when(bindingResult.getFieldErrors()).thenReturn(java.util.List.of(fieldError));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        // Act
        ResponseEntity<ErrorResponse> response = mapper.handle(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).contains("testField");
    }

    @Test
    void givenConstraintViolationException_whenHandle_thenReturn400WithViolationMessages() {
        // Arrange
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Constraint violation message");
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation);

        ConstraintViolationException exception = new ConstraintViolationException(violations);

        // Act
        ResponseEntity<ErrorResponse> response = mapper.handle(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().error()).contains("Constraint violation message");
    }
}
