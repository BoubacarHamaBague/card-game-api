package com.example.cardgame.domain.exception;

import com.example.cardgame.domain.util.ErrorCode;
import com.example.cardgame.domain.util.ErrorMessage;

public class InvalidCardException extends RuntimeException {
    private final ErrorCode errorCode;

    public InvalidCardException(String message) {
        super(message);
        this.errorCode = ErrorCode.INVALID_CARD;
    }

    public static InvalidCardException nullSuit() {
        return new InvalidCardException(ErrorMessage.SUIT_NULL);
    }

    public static InvalidCardException nullRank() {
        return new InvalidCardException(ErrorMessage.RANK_NULL);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
