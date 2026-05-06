package com.example.cardgame.interfaces.rest;

import com.example.cardgame.interfaces.rest.requests.AddPlayerRequest;
import com.example.cardgame.application.service.CardDealingService;
import com.example.cardgame.application.service.GameService;
import com.example.cardgame.application.service.DeckService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class StatisticsControllerTest {

    private static final String TEST_PLAYER_NAME = "Test";
    private static final String API_GAMES_PREFIX = "/api/games/";
    private static final String REMAINING_SUITS_SUFFIX = "/remaining/suits";
    private static final String REMAINING_CARDS_SUFFIX = "/remaining/cards";
    private static final String PLAYERS_SUFFIX = "/players";
    private static final int CARDS_PER_SUIT = 13;
    private static final int STANDARD_DECK_SIZE = 52;
    private static final int CARDS_TO_DEAL = 5;
    private static final int REMAINING_AFTER_DEAL = 47;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GameService gameManagement;

    @Autowired
    private DeckService deckManagement;

    @Autowired
    private CardDealingService cardDealing;

    private String gameId;

    @BeforeEach
    void setUp() {
        var game = gameManagement.createGame();
        gameId = game.getId();

        var deck = deckManagement.createDeck();
        deckManagement.addDeckToGame(gameId, deck.getId());
    }

    @Test
    void givenGameWithStandardDeck_whenGetRemainingSuitCount_thenReturnCorrectSuitCounts() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get(API_GAMES_PREFIX + gameId + REMAINING_SUITS_SUFFIX))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.hearts").value(CARDS_PER_SUIT))
            .andExpect(jsonPath("$.spades").value(CARDS_PER_SUIT))
            .andExpect(jsonPath("$.clubs").value(CARDS_PER_SUIT))
            .andExpect(jsonPath("$.diamonds").value(CARDS_PER_SUIT));
    }

    @Test
    void givenGameWithDeckAfterCardsDeal_whenGetRemainingSuitCount_thenReturnValidSuitCounts() throws Exception {
        // Arrange
        String addPlayerReq = objectMapper.writeValueAsString(new AddPlayerRequest(TEST_PLAYER_NAME));
        var resp = mockMvc.perform(post(API_GAMES_PREFIX + gameId + PLAYERS_SUFFIX)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addPlayerReq))
            .andReturn();

        String playerId = objectMapper.readTree(resp.getResponse().getContentAsString()).get("id").asText();
        cardDealing.dealCards(gameId, playerId, CARDS_TO_DEAL);

        // Act & Assert
        mockMvc.perform(get(API_GAMES_PREFIX + gameId + REMAINING_SUITS_SUFFIX))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.hearts").isNumber())
            .andExpect(jsonPath("$.hearts").value(org.hamcrest.Matchers.greaterThanOrEqualTo(0)))
            .andExpect(jsonPath("$.hearts").value(org.hamcrest.Matchers.lessThanOrEqualTo(CARDS_PER_SUIT)))
            .andExpect(jsonPath("$.spades").isNumber())
            .andExpect(jsonPath("$.spades").value(org.hamcrest.Matchers.greaterThanOrEqualTo(0)))
            .andExpect(jsonPath("$.spades").value(org.hamcrest.Matchers.lessThanOrEqualTo(CARDS_PER_SUIT)))
            .andExpect(jsonPath("$.clubs").isNumber())
            .andExpect(jsonPath("$.clubs").value(org.hamcrest.Matchers.greaterThanOrEqualTo(0)))
            .andExpect(jsonPath("$.clubs").value(org.hamcrest.Matchers.lessThanOrEqualTo(CARDS_PER_SUIT)))
            .andExpect(jsonPath("$.diamonds").isNumber())
            .andExpect(jsonPath("$.diamonds").value(org.hamcrest.Matchers.greaterThanOrEqualTo(0)))
            .andExpect(jsonPath("$.diamonds").value(org.hamcrest.Matchers.lessThanOrEqualTo(CARDS_PER_SUIT)));
    }

    @Test
    void givenGameWithStandardDeck_whenGetRemainingCards_thenReturn52CardsWithDetails() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get(API_GAMES_PREFIX + gameId + REMAINING_CARDS_SUFFIX))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total").value(STANDARD_DECK_SIZE))
            .andExpect(jsonPath("$.cards", hasSize(STANDARD_DECK_SIZE)))
            .andExpect(jsonPath("$.cards[0].suit").isNotEmpty())
            .andExpect(jsonPath("$.cards[0].rank").isNotEmpty())
            .andExpect(jsonPath("$.cards[0].value").isNumber());
    }

    @Test
    void givenGameWithDeckAfterCardsDeal_whenGetRemainingCards_thenReturnUpdatedCardCount() throws Exception {
        // Arrange
        String addPlayerReq = objectMapper.writeValueAsString(new AddPlayerRequest(TEST_PLAYER_NAME));
        var resp = mockMvc.perform(post(API_GAMES_PREFIX + gameId + PLAYERS_SUFFIX)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addPlayerReq))
            .andReturn();

        String playerId = objectMapper.readTree(resp.getResponse().getContentAsString()).get("id").asText();
        cardDealing.dealCards(gameId, playerId, CARDS_TO_DEAL);

        // Act & Assert
        mockMvc.perform(get(API_GAMES_PREFIX + gameId + REMAINING_CARDS_SUFFIX))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total").value(REMAINING_AFTER_DEAL))
            .andExpect(jsonPath("$.cards", hasSize(REMAINING_AFTER_DEAL)));
    }
}
