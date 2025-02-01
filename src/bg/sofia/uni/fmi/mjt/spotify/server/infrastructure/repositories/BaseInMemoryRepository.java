package bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.exception.EntityNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Identifiable;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.BaseRepository;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class BaseInMemoryRepository<T extends Identifiable> implements BaseRepository<T> {
    protected final Class<T> tClass;
    protected final Map<String, T> entities = new ConcurrentHashMap<>();
    protected final AtomicInteger lastId = new AtomicInteger();

    public BaseInMemoryRepository(Class<T> tClass) {
        this.tClass = tClass;
    }

    @Override
    public T getOrThrow(String id) {
        if (!entities.containsKey(id)) {
            throw new EntityNotFoundException("No " + tClass.getSimpleName() + " found with id " + id);
        }
        return entities.get(id);
    }

    @Override
    public T create(T toCreate) {
        toCreate.setId(String.valueOf(lastId.getAndIncrement()));
        entities.put(toCreate.getId(), toCreate);
        return toCreate;
    }

    @Override
    public T updateOrThrow(T toUpdate) {
        if (!entities.containsKey(toUpdate.getId())) {
            throw new EntityNotFoundException("No " + tClass.getSimpleName() + " found with id " + toUpdate.getId());
        }
        entities.put(toUpdate.getId(), toUpdate);
        return toUpdate;
    }

    @Override
    public Set<T> getAll() {
        return Set.copyOf(entities.values());
    }
}
