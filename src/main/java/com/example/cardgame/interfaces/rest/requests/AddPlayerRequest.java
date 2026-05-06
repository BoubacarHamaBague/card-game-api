package com.example.cardgame.interfaces.rest.requests;

import jakarta.validation.constraints.NotBlank;

public record AddPlayerRequest(
    @NotBlank(message = "Player name must not be blank") String name
) {
}
