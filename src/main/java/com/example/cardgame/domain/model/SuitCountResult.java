package com.example.cardgame.domain.model;

import java.util.Map;

public record SuitCountResult(Map<String, Integer> counts) {
    public SuitCountResult {
        counts = Map.copyOf(counts);
    }
}
