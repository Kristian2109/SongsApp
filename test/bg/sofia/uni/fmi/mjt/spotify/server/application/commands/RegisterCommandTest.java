package bg.sofia.uni.fmi.mjt.spotify.server.application.commands;

import bg.sofia.uni.fmi.mjt.spotify.server.application.HashingService;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.User;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegisterCommandTest {
    private UserRepository userRepository;
    private HashingService hashingService;
    private RegisterCommand command;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        hashingService = mock(HashingService.class);
    }

    @Test
    void testExecuteRegistersUserSuccessfully() {
        String email = "test@example.com";
        String password = "password123";
        String hashedPassword = "hashedPassword123";
        User mockUser = new User(null, email, hashedPassword);

        when(userRepository.getByEmail(email)).thenReturn(Optional.empty());
        when(hashingService.strongHash(password)).thenReturn(hashedPassword);
        when(userRepository.create(any(User.class))).thenReturn(mockUser);

        command = new RegisterCommand(userRepository, hashingService, email, password);

        User createdUser = command.execute();

        assertNotNull(createdUser);
        assertEquals(email, createdUser.getEmail());
        verify(userRepository).create(any(User.class));
    }

    @Test
    void testExecuteThrowsWhenUserAlreadyExists() {
        String email = "existing@example.com";
        when(userRepository.getByEmail(email)).thenReturn(Optional.of(new User(null, email, "hashedPass")));

        command = new RegisterCommand(userRepository, hashingService, email, "password");

        assertThrows(IllegalArgumentException.class, command::execute);
    }
}
