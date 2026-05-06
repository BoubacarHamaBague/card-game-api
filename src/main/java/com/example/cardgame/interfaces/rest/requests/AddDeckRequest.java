package com.example.cardgame.interfaces.rest.requests;

import jakarta.validation.constraints.NotBlank;

public record AddDeckRequest(
    @NotBlank(message = "Deck ID must not be blank")
    String deckId
) {
}
