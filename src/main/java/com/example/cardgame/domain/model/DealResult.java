package com.example.cardgame.domain.model;

import java.util.List;

public record DealResult(List<Card> dealt, GameDeck remaining) {}
