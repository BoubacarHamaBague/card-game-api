package com.example.cardgame.interfaces.rest.mappers;

import com.example.cardgame.domain.exception.InvalidCardException;
import com.example.cardgame.domain.exception.InvalidDeckException;
import com.example.cardgame.domain.exception.InvalidGameDeckException;
import com.example.cardgame.domain.exception.InvalidGameException;
import com.example.cardgame.domain.exception.InvalidPlayerException;
import com.example.cardgame.interfaces.rest.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
@Order(4)
public class InvalidParameterExceptionMapper {

    @ExceptionHandler({
        IllegalArgumentException.class,
        MethodArgumentNotValidException.class,
        ConstraintViolationException.class,
        InvalidCardException.class,
        InvalidPlayerException.class,
        InvalidDeckException.class,
        InvalidGameDeckException.class,
        InvalidGameException.class
    })
    public ResponseEntity<ErrorResponse> handle(Exception ex) {
        String message = ex instanceof MethodArgumentNotValidException mave
            ? mave.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "))
            : ex instanceof ConstraintViolationException cve
            ? cve.getConstraintViolations().stream()
                .map(cv -> cv.getMessage())
                .collect(Collectors.joining(", "))
            : ex.getMessage();

        String errorCode = null;
        if (ex instanceof InvalidCardException ice && ice.getErrorCode() != null) {
            errorCode = ice.getErrorCode().name();
        } else if (ex instanceof InvalidPlayerException ipe && ipe.getErrorCode() != null) {
            errorCode = ipe.getErrorCode().name();
        } else if (ex instanceof InvalidDeckException idke && idke.getErrorCode() != null) {
            errorCode = idke.getErrorCode().name();
        } else if (ex instanceof InvalidGameDeckException igde && igde.getErrorCode() != null) {
            errorCode = igde.getErrorCode().name();
        } else if (ex instanceof InvalidGameException ige && ige.getErrorCode() != null) {
            errorCode = ige.getErrorCode().name();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(message, errorCode));
    }
}
