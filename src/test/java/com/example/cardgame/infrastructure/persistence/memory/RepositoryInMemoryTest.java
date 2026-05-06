package com.example.cardgame.infrastructure.persistence.memory;

import com.example.cardgame.domain.util.IIdentifiable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class RepositoryInMemoryTest {

    private RepositoryInMemory<TestEntity> repository;

    @BeforeEach
    void setUp() {
        repository = new RepositoryInMemory<>();
    }

    @Test
    void givenNewRepository_whenSaveEntity_thenEntityIsStored() {
        // Arrange
        TestEntity entity = new TestEntity("id-1", "Entity 1");

        // Act
        TestEntity saved = repository.save(entity);

        // Assert
        assertThat(saved).isEqualTo(entity);
        assertThat(repository.findById("id-1")).isPresent().contains(entity);
    }

    @Test
    void givenStoredEntity_whenFindById_thenReturnEntity() {
        // Arrange
        TestEntity entity = new TestEntity("id-1", "Entity 1");
        repository.save(entity);

        // Act
        Optional<TestEntity> found = repository.findById("id-1");

        // Assert
        assertThat(found).isPresent().contains(entity);
    }

    @Test
    void givenNonexistentId_whenFindById_thenReturnEmpty() {
        // Act
        Optional<TestEntity> found = repository.findById("nonexistent");

        // Assert
        assertThat(found).isEmpty();
    }

    @Test
    void givenStoredEntity_whenDeleteById_thenEntityIsRemoved() {
        // Arrange
        TestEntity entity = new TestEntity("id-1", "Entity 1");
        repository.save(entity);

        // Act
        repository.deleteById("id-1");

        // Assert
        assertThat(repository.findById("id-1")).isEmpty();
    }

    @Test
    void givenMultipleEntities_whenGetAll_thenReturnAllEntities() {
        // Arrange
        TestEntity entity1 = new TestEntity("id-1", "Entity 1");
        TestEntity entity2 = new TestEntity("id-2", "Entity 2");
        repository.save(entity1);
        repository.save(entity2);

        // Act
        Collection<TestEntity> all = repository.getAll();

        // Assert
        assertThat(all).hasSize(2).contains(entity1, entity2);
    }

    @Test
    void givenStoredEntity_whenExists_thenReturnTrue() {
        // Arrange
        TestEntity entity = new TestEntity("id-1", "Entity 1");
        repository.save(entity);

        // Act & Assert
        assertThat(repository.exists("id-1")).isTrue();
        assertThat(repository.exists("nonexistent")).isFalse();
    }

    @Test
    void givenRepositoryWithEntities_whenClear_thenRepositoryIsEmpty() {
        // Arrange
        repository.save(new TestEntity("id-1", "Entity 1"));
        repository.save(new TestEntity("id-2", "Entity 2"));

        // Act
        repository.clear();

        // Assert
        assertThat(repository.getAll()).isEmpty();
    }

    @Test
    void givenStoredEntity_whenUpdateAtomically_thenEntityIsUpdated() {
        // Arrange
        TestEntity original = new TestEntity("id-1", "Original");
        repository.save(original);

        // Act
        Optional<TestEntity> updated = repository.updateAtomically("id-1",
            entity -> new TestEntity(entity.getId(), "Updated"));

        // Assert
        assertThat(updated).isPresent();
        assertThat(updated.get().getName()).isEqualTo("Updated");
        assertThat(repository.findById("id-1").get().getName()).isEqualTo("Updated");
    }

    @Test
    void givenNonexistentId_whenUpdateAtomically_thenReturnEmpty() {
        // Act
        Optional<TestEntity> result = repository.updateAtomically("nonexistent",
            entity -> entity);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void givenRepositoryWithMultipleEntities_whenConcurrentOperations_thenAllOperationsAreSafe() throws InterruptedException {
        // Arrange
        repository.save(new TestEntity("id-1", "Entity 1"));

        // Act
        Thread thread1 = new Thread(() ->
            repository.updateAtomically("id-1", entity -> new TestEntity(entity.getId(), "Updated by T1")));
        Thread thread2 = new Thread(() ->
            repository.updateAtomically("id-1", entity -> new TestEntity(entity.getId(), "Updated by T2")));

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        // Assert
        assertThat(repository.findById("id-1")).isPresent();
        assertThat(repository.getAll()).hasSize(1);
    }

    static class TestEntity implements IIdentifiable {
        private final String id;
        private final String name;

        TestEntity(String id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TestEntity that)) return false;
            return id.equals(that.id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }
}
