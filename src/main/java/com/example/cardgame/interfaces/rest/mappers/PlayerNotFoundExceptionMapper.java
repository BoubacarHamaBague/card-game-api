package com.example.cardgame.interfaces.rest.mappers;

import com.example.cardgame.domain.exception.PlayerNotFoundException;
import com.example.cardgame.interfaces.rest.dto.ErrorResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(2)
public class PlayerNotFoundExceptionMapper {

    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(PlayerNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(ex.getMessage(), ex.getErrorCode().name()));
    }
}
