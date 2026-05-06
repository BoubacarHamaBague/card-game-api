package com.example.cardgame.application.port;

import com.example.cardgame.domain.model.Card;
import java.util.List;

public interface ICardDealingService {
    List<Card> dealCards(String gameId, String playerId, int count);
}
