package com.example.cardgame.domain.exception;

import com.example.cardgame.domain.util.ErrorCode;
import com.example.cardgame.domain.util.ErrorMessage;

public class InvalidGameDeckException extends RuntimeException {
    private final ErrorCode errorCode;

    public InvalidGameDeckException(String message) {
        super(message);
        this.errorCode = null;
    }

    private InvalidGameDeckException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public static InvalidGameDeckException nullCards() {
        return new InvalidGameDeckException(ErrorMessage.CARDS_NULL);
    }

    public static InvalidGameDeckException nullDeck() {
        return new InvalidGameDeckException(ErrorMessage.DECK_NULL);
    }

    public static InvalidGameDeckException negativeCount() {
        return new InvalidGameDeckException(ErrorMessage.INVALID_COUNT, ErrorCode.INVALID_COUNT);
    }

    public static InvalidGameDeckException nullRandom() {
        return new InvalidGameDeckException(ErrorMessage.RANDOM_NULL);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
