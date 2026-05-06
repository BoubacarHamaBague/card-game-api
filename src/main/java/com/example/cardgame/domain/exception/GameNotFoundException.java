package com.example.cardgame.domain.exception;

import com.example.cardgame.domain.util.ErrorCode;

public class GameNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public GameNotFoundException(String gameId) {
        super("Game not found: " + gameId);
        this.errorCode = ErrorCode.GAME_NOT_FOUND;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
