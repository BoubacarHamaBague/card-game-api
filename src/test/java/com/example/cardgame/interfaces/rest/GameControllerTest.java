package com.example.cardgame.interfaces.rest;

import com.example.cardgame.application.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTest {

    private static final String NONEXISTENT_ID = "nonexistent";
    private static final String API_GAMES_ENDPOINT = "/api/games";
    private static final String SHUFFLE_SUFFIX = "/shuffle";
    private static final int EMPTY_REMAINING_CARDS = 0;
    private static final int EMPTY_PLAYER_COUNT = 0;
    private static final String GAME_NOT_FOUND_TEXT = "Game not found";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameService gameManagement;

    @Test
    void givenGameCreationRequest_whenCreateGame_thenGameIsCreatedWithCorrectProperties() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(post(API_GAMES_ENDPOINT))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.remainingCards").value(EMPTY_REMAINING_CARDS))
            .andExpect(jsonPath("$.playerCount").value(EMPTY_PLAYER_COUNT));
    }

    @Test
    void givenExistingGame_whenDeleteGame_thenGameIsDeleted() throws Exception {
        // Arrange
        var game = gameManagement.createGame();

        // Act & Assert
        mockMvc.perform(delete(API_GAMES_ENDPOINT + "/" + game.getId()))
            .andExpect(status().isNoContent());
    }

    @Test
    void givenNonexistentGameId_whenDeleteGame_thenReturnNotFound() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(delete(API_GAMES_ENDPOINT + "/" + NONEXISTENT_ID))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value(containsString(GAME_NOT_FOUND_TEXT)));
    }

    @Test
    void givenExistingGameAndShuffleRequest_whenShuffleGame_thenGameIsShuffled() throws Exception {
        // Arrange
        var game = gameManagement.createGame();

        // Act & Assert
        mockMvc.perform(post(API_GAMES_ENDPOINT + "/" + game.getId() + SHUFFLE_SUFFIX))
            .andExpect(status().isNoContent());
    }
}
