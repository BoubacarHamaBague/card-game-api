package com.example.cardgame.domain.repository;

import com.example.cardgame.domain.model.Deck;
import java.util.Optional;

public interface IDeckRepository {
    Deck save(Deck deck);
    Optional<Deck> findById(String deckId);

    void deleteByIdOrThrow(String deckId);
}
