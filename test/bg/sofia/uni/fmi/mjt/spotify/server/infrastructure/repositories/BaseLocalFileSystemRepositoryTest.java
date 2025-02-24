package bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.exception.EntityNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Identifiable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BaseLocalFileSystemRepositoryTest {
    private static final String ENTITY_ID = "1";
    private static final String ENTITY_NAME = "Test Entity";

    private BaseLocalFileSystemRepository<TestEntity> repository;
    private TestEntity testEntity;

    @BeforeEach
    void setUp() {
        testEntity = new TestEntity(ENTITY_ID, ENTITY_NAME);
        repository = new BaseLocalFileSystemRepository<>(TestEntity.class, List.of(testEntity));
    }

    @Test
    void testAlternativeConstructorInitializesEntitiesCorrectly() {
        Set<TestEntity> result = repository.getAll();

        assertEquals(1, result.size(), "The repository should contain one entity.");
        assertTrue(result.contains(testEntity), "The result should contain the test entity.");
    }

    @Test
    void testGetOrThrowReturnsEntityWhenExists() {
        TestEntity result = repository.getOrThrow(ENTITY_ID);

        assertEquals(testEntity, result, "The returned entity should match the expected entity.");
    }

    @Test
    void testGetOrThrowThrowsEntityNotFoundExceptionWhenEntityDoesNotExist() {
        String nonExistentId = "999";

        assertThrows(EntityNotFoundException.class, () -> repository.getOrThrow(nonExistentId),
            "Expected EntityNotFoundException for non-existent entity.");
    }

    @Test
    void testCreateAddsEntityWithAutoIncrementedId() {
        TestEntity newEntity = new TestEntity(null, "New Entity");

        TestEntity createdEntity = repository.create(newEntity);

        assertNotNull(createdEntity.getId(), "The created entity should have an ID.");
        assertEquals("2", createdEntity.getId(), "The ID should be auto-incremented.");
        assertEquals(newEntity.getName(), createdEntity.getName(), "The entity name should match.");
        assertTrue(repository.getAll().contains(createdEntity), "The repository should contain the created entity.");
    }

    @Test
    void testUpdateOrThrowUpdatesEntityWhenExists() {
        TestEntity updatedEntity = new TestEntity(ENTITY_ID, "Updated Entity");

        TestEntity result = repository.updateOrThrow(updatedEntity);

        assertEquals(updatedEntity, result, "The updated entity should match the input.");
        assertEquals(updatedEntity, repository.getOrThrow(ENTITY_ID), "The repository should contain the updated entity.");
    }

    @Test
    void testUpdateOrThrowThrowsEntityNotFoundExceptionWhenEntityDoesNotExist() {
        TestEntity nonExistentEntity = new TestEntity("999", "Non-Existent Entity");

        assertThrows(EntityNotFoundException.class, () -> repository.updateOrThrow(nonExistentEntity),
            "Expected EntityNotFoundException for non-existent entity.");
    }

    @Test
    void testGetAllReturnsAllEntities() {
        Set<TestEntity> result = repository.getAll();

        assertEquals(1, result.size(), "The repository should contain one entity.");
        assertTrue(result.contains(testEntity), "The result should contain the test entity.");
    }

    private static class TestEntity extends Identifiable {
        private final String name;

        public TestEntity(String id, String name) {
            super(id);
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}