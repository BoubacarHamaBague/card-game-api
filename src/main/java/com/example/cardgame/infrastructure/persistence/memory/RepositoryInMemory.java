package com.example.cardgame.infrastructure.persistence.memory;

import com.example.cardgame.domain.util.IIdentifiable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class RepositoryInMemory<T extends IIdentifiable> {

    private final Map<String, T> store = Collections.synchronizedMap(new HashMap<>());

    public T save(T entity) {
        store.put(entity.getId(), entity);
        return entity;
    }

    public Optional<T> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public void deleteById(String id) {
        store.remove(id);
    }

    public void deleteByIdOrThrow(String id, RuntimeException exception) {
        synchronized (store) {
            if (!store.containsKey(id)) {
                throw exception;
            }
            store.remove(id);
        }
    }

    public Collection<T> getAll() {
        return Collections.unmodifiableCollection(store.values());
    }

    public boolean exists(String id) {
        return store.containsKey(id);
    }

    public void clear() {
        store.clear();
    }

    public Optional<T> updateAtomically(String id, Function<T, T> updater) {
        synchronized (store) {
            T current = store.get(id);
            if (current == null) {
                return Optional.empty();
            }
            T updated = updater.apply(current);
            store.put(id, updated);
            return Optional.of(updated);
        }
    }
}
