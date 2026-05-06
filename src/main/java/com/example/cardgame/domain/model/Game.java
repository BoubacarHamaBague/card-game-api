package com.example.cardgame.domain.model;

import com.example.cardgame.domain.exception.InvalidGameException;
import com.example.cardgame.domain.exception.PlayerNotFoundException;
import com.example.cardgame.domain.util.IIdentifiable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class Game implements IIdentifiable {
    private final String id;
    private final GameDeck gameDeck;
    private final Map<String, Player> players;

    public Game(String id, GameDeck gameDeck, Map<String, Player> players) {
        if (id == null) throw InvalidGameException.nullId();
        if (gameDeck == null) throw InvalidGameException.nullGameDeck();
        if (players == null) throw InvalidGameException.nullPlayers();

        this.id = id;
        this.gameDeck = gameDeck;
        this.players = Collections.unmodifiableMap(new HashMap<>(players));
    }

    public static Game empty() {
        return new Game(UUID.randomUUID().toString(), GameDeck.empty(), new HashMap<>());
    }

    public String getId() {
        return id;
    }

    public GameDeck getGameDeck() {
        return gameDeck;
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public boolean hasPlayer(String playerId) {
        if (playerId == null) throw InvalidGameException.nullPlayerId();
        return players.containsKey(playerId);
    }

    public Player getPlayer(String playerId) {
        if (playerId == null) throw InvalidGameException.nullPlayerId();
        Player player = players.get(playerId);
        if (player == null) throw new PlayerNotFoundException(playerId);
        return player;
    }

    public Game addPlayer(Player player) {
        if (player == null) throw InvalidGameException.nullPlayer();
        if (players.containsKey(player.getId())) {
            throw InvalidGameException.playerAlreadyExists(player.getId());
        }
        Map<String, Player> updated = new HashMap<>(players);
        updated.put(player.getId(), player);
        return new Game(id, gameDeck, updated);
    }

    public Game removePlayer(String playerId) {
        if (playerId == null) throw InvalidGameException.nullPlayerId();
        if (!players.containsKey(playerId)) {
            throw new PlayerNotFoundException(playerId);
        }
        Map<String, Player> updated = new HashMap<>(players);
        updated.remove(playerId);
        return new Game(id, gameDeck, updated);
    }

    public Game withGameDeck(GameDeck gameDeck) {
        if (gameDeck == null) throw InvalidGameException.nullGameDeck();
        return new Game(id, gameDeck, players);
    }

    public Game withPlayer(Player player) {
        if (player == null) throw InvalidGameException.nullPlayer();
        Map<String, Player> updated = new HashMap<>(players);
        updated.put(player.getId(), player);
        return new Game(id, gameDeck, updated);
    }

    public List<Player> getPlayersSortedByHandValueDesc() {
        return players.values().stream()
                .sorted((p1, p2) -> Integer.compare(p2.getHandValue(), p1.getHandValue()))
                .toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game game)) return false;
        return id.equals(game.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Game{id='" + id + "', gameDeck.size=" + gameDeck.getRemainingCount() + ", players=" + players.size() + "}";
    }
}
