package com.example.cardgame.domain.exception;

import com.example.cardgame.domain.util.ErrorCode;
import com.example.cardgame.domain.util.ErrorMessage;

public class InvalidDeckException extends RuntimeException {
    private final ErrorCode errorCode;

    public InvalidDeckException(String message) {
        super(message);
        this.errorCode = ErrorCode.INVALID_DECK_ID;
    }

    private InvalidDeckException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public static InvalidDeckException nullId() {
        return new InvalidDeckException(ErrorMessage.DECK_ID_NULL);
    }

    public static InvalidDeckException nullCards() {
        return new InvalidDeckException(ErrorMessage.CARDS_NULL);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
