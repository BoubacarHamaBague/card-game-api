package com.example.cardgame.domain.exception;

import com.example.cardgame.domain.util.ErrorCode;
import com.example.cardgame.domain.util.ErrorMessage;

public class InvalidPlayerException extends RuntimeException {
    private final ErrorCode errorCode;

    public InvalidPlayerException(String message) {
        super(message);
        this.errorCode = ErrorCode.INVALID_PLAYER_NAME;
    }

    private InvalidPlayerException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public static InvalidPlayerException nullId() {
        return new InvalidPlayerException(ErrorMessage.PLAYER_ID_NULL, ErrorCode.INVALID_PLAYER_ID);
    }

    public static InvalidPlayerException nullName() {
        return new InvalidPlayerException(ErrorMessage.PLAYER_NAME_NULL, ErrorCode.INVALID_PLAYER_NAME);
    }

    public static InvalidPlayerException nullHand() {
        return new InvalidPlayerException(ErrorMessage.PLAYER_HAND_NULL, ErrorCode.INVALID_PLAYER_HAND);
    }

    public static InvalidPlayerException nullNewCards() {
        return new InvalidPlayerException(ErrorMessage.NEW_CARDS_NULL, ErrorCode.INVALID_NEW_CARDS);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
