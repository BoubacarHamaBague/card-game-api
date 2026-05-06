package com.example.cardgame.interfaces.rest.dto;

import java.util.Map;

public record SuitCountResponse(
    int hearts,
    int spades,
    int clubs,
    int diamonds
) {
    public static SuitCountResponse from(Map<String, Integer> counts) {
        return new SuitCountResponse(
            counts.getOrDefault("hearts", 0),
            counts.getOrDefault("spades", 0),
            counts.getOrDefault("clubs", 0),
            counts.getOrDefault("diamonds", 0)
        );
    }
}
