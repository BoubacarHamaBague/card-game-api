package com.example.cardgame.domain.repository;

import com.example.cardgame.domain.model.Game;
import java.util.Optional;
import java.util.function.Function;

public interface IGameRepository {
    Game save(Game game);
    Optional<Game> findById(String gameId);

    void deleteByIdOrThrow(String gameId);

    Optional<Game> updateAtomically(String gameId, Function<Game, Game> updater);
}
