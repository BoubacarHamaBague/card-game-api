package com.example.cardgame.domain.exception;

import com.example.cardgame.domain.util.ErrorCode;

public class DeckNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public DeckNotFoundException(String deckId) {
        super("Deck not found: " + deckId);
        this.errorCode = ErrorCode.DECK_NOT_FOUND;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
