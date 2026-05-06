package com.example.cardgame.domain.exception;

import com.example.cardgame.domain.util.ErrorCode;
import com.example.cardgame.domain.util.ErrorMessage;

public class InvalidGameException extends RuntimeException {
    private final ErrorCode errorCode;

    public InvalidGameException(String message) {
        super(message);
        this.errorCode = null;
    }

    private InvalidGameException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public static InvalidGameException nullId() {
        return new InvalidGameException(ErrorMessage.GAME_ID_NULL);
    }

    public static InvalidGameException nullGameDeck() {
        return new InvalidGameException(ErrorMessage.GAME_DECK_NULL);
    }

    public static InvalidGameException nullPlayers() {
        return new InvalidGameException(ErrorMessage.PLAYERS_NULL);
    }

    public static InvalidGameException nullPlayerId() {
        return new InvalidGameException(ErrorMessage.PLAYER_ID_NULL);
    }

    public static InvalidGameException nullPlayer() {
        return new InvalidGameException(ErrorMessage.PLAYER_NULL);
    }

    public static InvalidGameException playerAlreadyExists(String playerId) {
        return new InvalidGameException("Player already exists: " + playerId, ErrorCode.PLAYER_ALREADY_EXISTS);
    }

    public static InvalidGameException playerNotFound(String playerId) {
        return new InvalidGameException("Player not found: " + playerId, ErrorCode.PLAYER_NOT_IN_GAME);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
