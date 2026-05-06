package com.example.cardgame.interfaces.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class RestExceptionHandlerTest {

    private static final String NONEXISTENT_ID = "nonexistent";
    private static final String SOME_GAME_ID = "someId";
    private static final String GAME_ID = "gameId";
    private static final String PLAYER_ID = "playerId";
    private static final String DECK_ID = "deckId";
    private static final String PLAYER_NAME_ALICE = "Alice";
    private static final String EMPTY_STRING = "";
    private static final String APPLICATION_JSON = "application/json";
    private static final String API_GAMES_ENDPOINT = "/api/games";
    private static final String DECKS_SUFFIX = "/decks";
    private static final String PLAYERS_SUFFIX = "/players";
    private static final String DEAL_SUFFIX = "/deal";
    private static final String CARDS_SUFFIX = "/cards";
    private static final String RANKING_SUFFIX = "/ranking";
    private static final String REMAINING_PREFIX = "/remaining";
    private static final String SUITS_SUFFIX = "/suits";
    private static final String CARDS_REMAINING_SUFFIX = "/cards";
    private static final String SHUFFLE_SUFFIX = "/shuffle";
    private static final String COUNT_PARAM = "?count=5";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenNonexistentGameId_whenDeleteGame_thenReturnNotFoundWithError() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(delete(API_GAMES_ENDPOINT + "/" + NONEXISTENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void givenNonexistentGameIdAndDeckId_whenAddDeck_thenReturnNotFound() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(post(API_GAMES_ENDPOINT + "/" + NONEXISTENT_ID + DECKS_SUFFIX)
                .contentType(APPLICATION_JSON)
                .content("{\"" + DECK_ID + "\":\"" + DECK_ID + "\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNonexistentGameIdAndPlayerName_whenAddPlayer_thenReturnNotFound() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(post(API_GAMES_ENDPOINT + "/" + NONEXISTENT_ID + PLAYERS_SUFFIX)
                .contentType(APPLICATION_JSON)
                .content("{\"name\":\"" + PLAYER_NAME_ALICE + "\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNonexistentGameIdAndPlayerId_whenDealCards_thenReturnNotFound() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(post(API_GAMES_ENDPOINT + "/" + NONEXISTENT_ID + PLAYERS_SUFFIX + "/" + PLAYER_ID + DEAL_SUFFIX + COUNT_PARAM))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNonexistentGameIdAndPlayerId_whenGetPlayerCards_thenReturnNotFound() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get(API_GAMES_ENDPOINT + "/" + NONEXISTENT_ID + PLAYERS_SUFFIX + "/" + PLAYER_ID + CARDS_SUFFIX))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNonexistentGameId_whenGetRanking_thenReturnNotFound() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get(API_GAMES_ENDPOINT + "/" + NONEXISTENT_ID + PLAYERS_SUFFIX + RANKING_SUFFIX))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNonexistentGameId_whenGetRemainingSuits_thenReturnNotFound() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get(API_GAMES_ENDPOINT + "/" + NONEXISTENT_ID + REMAINING_PREFIX + SUITS_SUFFIX))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNonexistentGameId_whenGetRemainingCards_thenReturnNotFound() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get(API_GAMES_ENDPOINT + "/" + NONEXISTENT_ID + REMAINING_PREFIX + CARDS_REMAINING_SUFFIX))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenNonexistentGameId_whenShuffleGame_thenReturnNotFound() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(post(API_GAMES_ENDPOINT + "/" + NONEXISTENT_ID + SHUFFLE_SUFFIX))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenGameAndBlankPlayerName_whenAddPlayer_thenReturnBadRequest() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(post(API_GAMES_ENDPOINT + "/" + SOME_GAME_ID + PLAYERS_SUFFIX)
                .contentType(APPLICATION_JSON)
                .content("{\"name\":\"" + EMPTY_STRING + "\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenGameAndBlankDeckId_whenAddDeck_thenReturnBadRequest() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(post(API_GAMES_ENDPOINT + "/" + GAME_ID + DECKS_SUFFIX)
                .contentType(APPLICATION_JSON)
                .content("{\"" + DECK_ID + "\":\"" + EMPTY_STRING + "\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenNonexistentGameId_whenDeleteGame_thenReturnErrorCodeGameNotFound() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(delete(API_GAMES_ENDPOINT + "/" + NONEXISTENT_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.errorCode").value("GAME_NOT_FOUND"));
    }

    @Test
    void givenNonexistentGameIdAndPlayerId_whenGetPlayerCards_thenReturnErrorCodeGameNotFound() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get(API_GAMES_ENDPOINT + "/" + NONEXISTENT_ID + PLAYERS_SUFFIX + "/" + PLAYER_ID + CARDS_SUFFIX))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("GAME_NOT_FOUND"));
    }
}
