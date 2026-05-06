package com.example.cardgame.application.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Card Game API",
        version = "1.0.0",
        description = "REST API for a poker-style card game. Supports multiple players, multiple decks (shoe), dealing, shuffling and hand statistics.",
        contact = @Contact(name = "Card Game API", email = "support@cardgame.example.com")
    ),
    tags = {
        @Tag(name = "Game",       description = "Game lifecycle — create, delete, shuffle"),
        @Tag(name = "Players",    description = "Player management — add, remove, deal cards, hand and ranking"),
        @Tag(name = "Deck",       description = "Deck management — create a deck and add it to a game"),
        @Tag(name = "Statistics", description = "Remaining cards in the game deck — by suit or detailed")
    }
)
public class OpenApiConfig {}
