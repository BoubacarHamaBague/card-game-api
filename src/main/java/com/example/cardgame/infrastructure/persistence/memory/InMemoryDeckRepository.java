package com.example.cardgame.infrastructure.persistence.memory;

import com.example.cardgame.domain.exception.DeckNotFoundException;
import com.example.cardgame.domain.model.Deck;
import com.example.cardgame.domain.repository.IDeckRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryDeckRepository implements IDeckRepository {

    private final RepositoryInMemory<Deck> repository = new RepositoryInMemory<>();

    @Override
    public Deck save(Deck deck) {
        return repository.save(deck);
    }

    @Override
    public Optional<Deck> findById(String deckId) {
        return repository.findById(deckId);
    }

    @Override
    public void deleteByIdOrThrow(String deckId) {
        repository.deleteByIdOrThrow(deckId, new DeckNotFoundException(deckId));
    }
}
