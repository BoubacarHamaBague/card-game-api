package com.example.cardgame.interfaces.rest;

import com.example.cardgame.interfaces.rest.requests.AddPlayerRequest;
import com.example.cardgame.application.service.PlayerService;
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
class PlayerControllerTest {

    private static final String PLAYER_NAME_ALICE = "Alice";
    private static final String PLAYER_NAME_BOB = "Bob";
    private static final String PLAYER_NAME_CHARLIE = "Charlie";
    private static final String PLAYER_NAME_DAVID = "David";
    private static final String PLAYER_NAME_EVE = "Eve";
    private static final String NONEXISTENT_GAME_ID = "nonexistent";
    private static final String API_GAMES_PREFIX = "/api/games/";
    private static final String PLAYERS_SUFFIX = "/players";
    private static final String DEAL_SUFFIX = "/deal";
    private static final String CARDS_SUFFIX = "/cards";
    private static final String RANKING_SUFFIX = "/ranking";
    private static final String COUNT_PARAM = "?count=";
    private static final int CARDS_TO_DEAL_2 = 2;
    private static final int CARDS_TO_DEAL_3 = 3;
    private static final int CARDS_TO_DEAL_5 = 5;
    private static final int CARDS_TO_DEAL_100 = 100;
    private static final int EMPTY_HAND_SIZE = 0;
    private static final int EMPTY_TOTAL_DEALT = 0;
    private static final int EXPECTED_RANKING_SIZE = 2;
    private static final int RANKING_POSITION_1 = 1;
    private static final int RANKING_POSITION_2 = 2;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GameService gameManagement;

    @Autowired
    private DeckService deckManagement;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private CardDealingService cardDealingService;

    private String gameId;
    private String deckId;

    @BeforeEach
    void setUp() {
        var game = gameManagement.createGame();
        gameId = game.getId();

        var deck = deckManagement.createDeck();
        deckId = deck.getId();

        deckManagement.addDeckToGame(gameId, deckId);
    }

    @Test
    void givenGameAndPlayerName_whenAddPlayer_thenPlayerIsAddedWithCorrectProperties() throws Exception {
        // Arrange
        String request = objectMapper.writeValueAsString(new AddPlayerRequest(PLAYER_NAME_ALICE));

        // Act & Assert
        mockMvc.perform(post(API_GAMES_PREFIX + gameId + PLAYERS_SUFFIX)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").value(PLAYER_NAME_ALICE))
            .andExpect(jsonPath("$.handValue").value(EMPTY_HAND_SIZE))
            .andExpect(jsonPath("$.cardCount").value(EMPTY_HAND_SIZE));
    }

    @Test
    void givenNonexistentGameAndPlayerName_whenAddPlayer_thenReturnNotFound() throws Exception {
        // Arrange
        String request = objectMapper.writeValueAsString(new AddPlayerRequest(PLAYER_NAME_BOB));

        // Act & Assert
        mockMvc.perform(post(API_GAMES_PREFIX + NONEXISTENT_GAME_ID + PLAYERS_SUFFIX)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value(containsString("Game not found")));
    }

    @Test
    void givenGameAndPlayerAndCardCount_whenDealCards_thenCardsAreDealt() throws Exception {
        // Arrange
        String addPlayerReq = objectMapper.writeValueAsString(new AddPlayerRequest(PLAYER_NAME_CHARLIE));
        var addResp = mockMvc.perform(post(API_GAMES_PREFIX + gameId + PLAYERS_SUFFIX)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addPlayerReq))
            .andReturn();

        String playerId = objectMapper.readTree(addResp.getResponse().getContentAsString()).get("id").asText();

        // Act & Assert
        mockMvc.perform(post(API_GAMES_PREFIX + gameId + PLAYERS_SUFFIX + "/" + playerId + DEAL_SUFFIX + COUNT_PARAM + CARDS_TO_DEAL_5))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.dealt", hasSize(CARDS_TO_DEAL_5)))
            .andExpect(jsonPath("$.totalDealt").value(CARDS_TO_DEAL_5));
    }

    @Test
    void givenGameAndPlayerAndCardCountMoreThanAvailable_whenDealCards_thenNothingIsDealt() throws Exception {
        // Arrange
        String addPlayerReq = objectMapper.writeValueAsString(new AddPlayerRequest(PLAYER_NAME_DAVID));
        var addResp = mockMvc.perform(post(API_GAMES_PREFIX + gameId + PLAYERS_SUFFIX)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addPlayerReq))
            .andReturn();

        String playerId = objectMapper.readTree(addResp.getResponse().getContentAsString()).get("id").asText();

        // Act & Assert
        mockMvc.perform(post(API_GAMES_PREFIX + gameId + PLAYERS_SUFFIX + "/" + playerId + DEAL_SUFFIX + COUNT_PARAM + CARDS_TO_DEAL_100))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.dealt", hasSize(EMPTY_HAND_SIZE)))
            .andExpect(jsonPath("$.totalDealt").value(EMPTY_TOTAL_DEALT));
    }

    @Test
    void givenGameAndPlayerWithCards_whenGetPlayerCards_thenReturnPlayerCards() throws Exception {
        // Arrange
        String addPlayerReq = objectMapper.writeValueAsString(new AddPlayerRequest(PLAYER_NAME_EVE));
        var addResp = mockMvc.perform(post(API_GAMES_PREFIX + gameId + PLAYERS_SUFFIX)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addPlayerReq))
            .andReturn();

        String playerId = objectMapper.readTree(addResp.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(post(API_GAMES_PREFIX + gameId + PLAYERS_SUFFIX + "/" + playerId + DEAL_SUFFIX + COUNT_PARAM + CARDS_TO_DEAL_3));

        // Act & Assert
        mockMvc.perform(get(API_GAMES_PREFIX + gameId + PLAYERS_SUFFIX + "/" + playerId + CARDS_SUFFIX))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.playerId").value(playerId))
            .andExpect(jsonPath("$.playerName").value(PLAYER_NAME_EVE))
            .andExpect(jsonPath("$.hand", hasSize(CARDS_TO_DEAL_3)))
            .andExpect(jsonPath("$.totalValue").isNumber());
    }

    @Test
    void givenGameAndPlayer_whenRemovePlayer_thenPlayerIsRemoved() throws Exception {
        // Arrange
        String addPlayerReq = objectMapper.writeValueAsString(new AddPlayerRequest(PLAYER_NAME_ALICE));
        var addResp = mockMvc.perform(post(API_GAMES_PREFIX + gameId + PLAYERS_SUFFIX)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addPlayerReq))
            .andReturn();
        String playerId = objectMapper.readTree(addResp.getResponse().getContentAsString()).get("id").asText();

        // Act & Assert
        mockMvc.perform(delete(API_GAMES_PREFIX + gameId + PLAYERS_SUFFIX + "/" + playerId))
            .andExpect(status().isNoContent());
    }

    @Test
    void givenNonexistentPlayer_whenRemovePlayer_thenReturnNotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(delete(API_GAMES_PREFIX + gameId + PLAYERS_SUFFIX + "/nonexistent-player"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value(containsString("Player not found")));
    }

    @Test
    void givenNegativeCount_whenDealCards_thenReturnBadRequest() throws Exception {
        // Arrange
        String addPlayerReq = objectMapper.writeValueAsString(new AddPlayerRequest(PLAYER_NAME_ALICE));
        var addResp = mockMvc.perform(post(API_GAMES_PREFIX + gameId + PLAYERS_SUFFIX)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addPlayerReq))
            .andReturn();
        String playerId = objectMapper.readTree(addResp.getResponse().getContentAsString()).get("id").asText();

        // Act & Assert
        mockMvc.perform(post(API_GAMES_PREFIX + gameId + PLAYERS_SUFFIX + "/" + playerId + DEAL_SUFFIX + "?count=-1"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void givenGameWithTwoPlayersAndDealtCards_whenGetRanking_thenReturnRankedPlayers() throws Exception {
        // Arrange
        String addReq1 = objectMapper.writeValueAsString(new AddPlayerRequest(PLAYER_NAME_ALICE));
        var resp1 = mockMvc.perform(post(API_GAMES_PREFIX + gameId + PLAYERS_SUFFIX)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addReq1))
            .andReturn();
        String playerId1 = objectMapper.readTree(resp1.getResponse().getContentAsString()).get("id").asText();

        String addReq2 = objectMapper.writeValueAsString(new AddPlayerRequest(PLAYER_NAME_BOB));
        var resp2 = mockMvc.perform(post(API_GAMES_PREFIX + gameId + PLAYERS_SUFFIX)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addReq2))
            .andReturn();
        String playerId2 = objectMapper.readTree(resp2.getResponse().getContentAsString()).get("id").asText();

        mockMvc.perform(post(API_GAMES_PREFIX + gameId + PLAYERS_SUFFIX + "/" + playerId1 + DEAL_SUFFIX + COUNT_PARAM + CARDS_TO_DEAL_3));
        mockMvc.perform(post(API_GAMES_PREFIX + gameId + PLAYERS_SUFFIX + "/" + playerId2 + DEAL_SUFFIX + COUNT_PARAM + CARDS_TO_DEAL_2));

        // Act & Assert
        mockMvc.perform(get(API_GAMES_PREFIX + gameId + PLAYERS_SUFFIX + RANKING_SUFFIX))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.ranking", hasSize(EXPECTED_RANKING_SIZE)))
            .andExpect(jsonPath("$.ranking[0].position").value(RANKING_POSITION_1))
            .andExpect(jsonPath("$.ranking[0].playerId").isNotEmpty())
            .andExpect(jsonPath("$.ranking[0].handValue").isNumber())
            .andExpect(jsonPath("$.ranking[1].position").value(RANKING_POSITION_2))
            .andExpect(jsonPath("$.ranking[1].playerId").isNotEmpty())
            .andExpect(jsonPath("$.ranking[1].handValue").isNumber());
    }
}
