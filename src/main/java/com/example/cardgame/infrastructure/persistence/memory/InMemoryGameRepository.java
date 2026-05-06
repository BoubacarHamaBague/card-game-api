package com.example.cardgame.infrastructure.persistence.memory;

import com.example.cardgame.domain.exception.GameNotFoundException;
import com.example.cardgame.domain.model.Game;
import com.example.cardgame.domain.repository.IGameRepository;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryGameRepository implements IGameRepository {

    private final RepositoryInMemory<Game> repository = new RepositoryInMemory<>();

    @Override
    public Game save(Game game) {
        return repository.save(game);
    }

    @Override
    public Optional<Game> findById(String gameId) {
        return repository.findById(gameId);
    }

    @Override
    public void deleteByIdOrThrow(String gameId) {
        repository.deleteByIdOrThrow(gameId, new GameNotFoundException(gameId));
    }

    @Override
    public Optional<Game> updateAtomically(String gameId, Function<Game, Game> updater) {
        return repository.updateAtomically(gameId, updater);
    }
}
