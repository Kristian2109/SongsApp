package bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories;

import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.Identifiable;

import java.util.Set;

public interface BaseRepository<T extends Identifiable> {
    T getOrThrow(String id);

    T create(T toCreate);

    T updateOrThrow(T toUpdate);

    Set<T> getAll();
}
