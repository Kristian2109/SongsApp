package bg.sofia.uni.fmi.mjt.spotify.server.application;

import bg.sofia.uni.fmi.mjt.spotify.server.application.exceptions.HashingException;

public interface HashingService {
    String strongHash(String s) throws HashingException;
}
