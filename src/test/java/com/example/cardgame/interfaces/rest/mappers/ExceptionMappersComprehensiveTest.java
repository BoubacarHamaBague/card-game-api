package com.example.cardgame.interfaces.rest.mappers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ExceptionMappersComprehensiveTest {

    private static final String NONEXISTENT_GAME_ID = "nonexistent-game";
    private static final String NONEXISTENT_PLAYER_ID = "nonexistent-player";
    private static final String NONEXISTENT_DECK_ID = "nonexistent-deck";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenGameNotFound_whenAccessGame_then404WithErrorMessage() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/games/" + NONEXISTENT_GAME_ID + "/remaining/suits"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void givenPlayerNotFound_whenAccessPlayer_then404WithErrorMessage() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/games/" + NONEXISTENT_GAME_ID + "/players/" + NONEXISTENT_PLAYER_ID + "/cards"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void givenInvalidDeckId_whenAddDeckToGame_thenNotFound() throws Exception {
        // Act & Assert - 404 (game not found)
        mockMvc.perform(post("/api/games/invalid/decks")
                .contentType("application/json")
                .content("{\"deckId\":\"" + NONEXISTENT_DECK_ID + "\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenBlankPlayerName_whenAddPlayer_then400WithErrorMessage() throws Exception {
        // Arrange
        var gameResp = mockMvc.perform(post("/api/games"))
                .andReturn();

        // Act & Assert
        mockMvc.perform(post("/api/games/any-game/players")
                .contentType("application/json")
                .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void givenBlankDeckId_whenAddDeckToGame_then400WithErrorMessage() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/games/any-game/decks")
                .contentType("application/json")
                .content("{\"deckId\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void givenMissingDeckIdField_whenAddDeckToGame_then400WithErrorMessage() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/games/any-game/decks")
                .contentType("application/json")
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void givenMissingNameField_whenAddPlayer_then400WithErrorMessage() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/games/any-game/players")
                .contentType("application/json")
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}
