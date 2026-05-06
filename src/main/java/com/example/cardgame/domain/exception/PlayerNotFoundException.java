package com.example.cardgame.domain.exception;

import com.example.cardgame.domain.util.ErrorCode;

public class PlayerNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public PlayerNotFoundException(String playerId) {
        super("Player not found: " + playerId);
        this.errorCode = ErrorCode.PLAYER_NOT_FOUND;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
