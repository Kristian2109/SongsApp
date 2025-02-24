package bg.sofia.uni.fmi.mjt.spotify.server.infrastructure.repositories;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.exception.EntityNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Identifiable;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.BaseRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class BaseLocalFileSystemRepository<T extends Identifiable> implements BaseRepository<T> {
    protected final Class<T> tClass;
    protected final AtomicInteger lastId = new AtomicInteger();
    protected Map<String, T> entities = new ConcurrentHashMap<>();
    protected Path sourceFile;
    public BaseLocalFileSystemRepository(Class<T> tClass, Path sourceFile) {
        this.tClass = tClass;
        this.sourceFile = sourceFile;
        List<T> entitiesList = readEntitiesFromFileSystem();
        int highestId = entitiesList.stream().mapToInt(e -> Integer.parseInt(e.getId())).max().orElse(0);
        lastId.set(highestId + 1);
        entitiesList.forEach(e -> entities.put(e.getId(), e));
    }

    public BaseLocalFileSystemRepository(Class<T> tClass, List<T> entities) {
        this.tClass = tClass;
        int highestId = entities.stream().mapToInt(e -> Integer.parseInt(e.getId())).max().orElse(0);
        lastId.set(highestId + 1);
        entities.forEach(e -> this.entities.put(e.getId(), e));
    }

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

    private List<T> readEntitiesFromFileSystem() {
        try (var reader = new FileReader(sourceFile.toString())) {
            Type type = TypeToken.getParameterized(List.class, tClass).getType();
            List<T> res = new Gson().fromJson(reader, type);
            if (res == null) {
                return List.of();
            }
            return res;
        } catch (IOException e) {
            throw new RuntimeException("Cannot read entities");
        }
    }

    public void saveEntitiesToFileSystem() {
        try (var writer = new FileWriter(sourceFile.toString())) {
            new Gson().toJson(entities.values(), writer);
        } catch (IOException e) {
            throw new RuntimeException("Cannot read entities");
        }
    }
}
