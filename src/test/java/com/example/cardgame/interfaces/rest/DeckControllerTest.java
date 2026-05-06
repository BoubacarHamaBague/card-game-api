package com.example.cardgame.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class DeckControllerTest {

    private static final String API_DECKS_ENDPOINT = "/api/decks";
    private static final String API_GAMES_PREFIX = "/api/games/";
    private static final String DECKS_SUFFIX = "/decks";
    private static final String GAME_ID = "gameId";
    private static final String APPLICATION_JSON = "application/json";
    private static final String DECK_ID_JSON_KEY = "deckId";
    private static final String EMPTY_STRING = "";
    private static final int STANDARD_DECK_SIZE = 52;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenDeckCreationRequest_whenCreateDeck_thenDeckIsCreatedWithCorrectSize() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(post(API_DECKS_ENDPOINT))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.size").value(STANDARD_DECK_SIZE));
    }

    @Test
    void givenGameAndDeck_whenAddDeckToGame_thenDeckIsAddedToGame() throws Exception {
        // Arrange
        var gameResp = mockMvc.perform(post(API_GAMES_PREFIX.replaceAll("/$", "")))
                .andExpect(status().isCreated())
                .andReturn();
        String gameId = objectMapper.readTree(gameResp.getResponse().getContentAsString()).get("id").asText();

        var deckResp = mockMvc.perform(post(API_DECKS_ENDPOINT))
                .andExpect(status().isCreated())
                .andReturn();
        String deckId = objectMapper.readTree(deckResp.getResponse().getContentAsString()).get("id").asText();

        // Act & Assert
        mockMvc.perform(post(API_GAMES_PREFIX + gameId + DECKS_SUFFIX)
                .contentType(APPLICATION_JSON)
                .content("{\"" + DECK_ID_JSON_KEY + "\":\"" + deckId + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void givenGameAndBlankDeckId_whenAddDeckToGame_thenReturnBadRequest() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(post(API_GAMES_PREFIX + GAME_ID + DECKS_SUFFIX)
                .contentType(APPLICATION_JSON)
                .content("{\"" + DECK_ID_JSON_KEY + "\":\"" + EMPTY_STRING + "\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenGameAndEmptyBody_whenAddDeckToGame_thenReturnBadRequest() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(post(API_GAMES_PREFIX + GAME_ID + DECKS_SUFFIX)
                .contentType(APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
