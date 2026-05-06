package com.example.cardgame.interfaces.rest.mappers;

import com.example.cardgame.interfaces.rest.dto.ErrorResponse;
import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
@Order(3)
public class HttpRequestExceptionMapper {

    @ExceptionHandler({
        HttpMessageNotReadableException.class,
        MissingServletRequestParameterException.class,
        HttpMediaTypeNotSupportedException.class,
        JsonParseException.class
    })
    public ResponseEntity<ErrorResponse> handleHttpRequestException(Exception ex) {
        String message;
        HttpStatus status = HttpStatus.BAD_REQUEST;

        if (ex instanceof MissingServletRequestParameterException msrp) {
            message = "Missing required parameter: " + msrp.getParameterName();
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            message = "Unsupported media type. Expected: application/json";
            status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        } else if (ex instanceof JsonParseException ||
                   ex instanceof HttpMessageNotReadableException ||
                   (ex.getCause() instanceof JsonParseException) ||
                   (ex.getMessage() != null &&
                    (ex.getMessage().contains("JSON") ||
                     ex.getMessage().contains("duplicate field")))) {
            message = "Invalid JSON format or missing required fields";
        } else {
            message = "Invalid request";
        }

        return ResponseEntity.status(status)
            .body(new ErrorResponse(message, null));
    }
}
