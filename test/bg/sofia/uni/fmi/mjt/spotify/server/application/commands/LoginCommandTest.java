package bg.sofia.uni.fmi.mjt.spotify.server.application.commands;

import bg.sofia.uni.fmi.mjt.spotify.server.application.HashingService;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.exception.EntityNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.User;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LoginCommandTest {
    private UserRepository userRepository;
    private HashingService hashingService;
    private LoginCommand command;
    private final String emailAddress = "test@example.com";
    private final String password = "password123";
    private final String hashedPassword = "hashedPassword123";

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        hashingService = mock(HashingService.class);
        command = new LoginCommand(userRepository, hashingService, emailAddress, password);
    }

    @Test
    void testExecuteSuccessfulLogin() {
        User mockUser = mock(User.class);
        when(userRepository.getByEmail(emailAddress)).thenReturn(Optional.of(mockUser));
        when(hashingService.strongHash(password)).thenReturn(hashedPassword);
        when(mockUser.comparePassword(hashedPassword)).thenReturn(true);

        Object result = command.execute();

        assertNull(result, "The result should be null for a successful login.");
        verify(userRepository).getByEmail(emailAddress);
        verify(hashingService).strongHash(password);
        verify(mockUser).comparePassword(hashedPassword);
    }

    @Test
    void testExecuteThrowsEntityNotFoundExceptionWhenUserNotFound() {
        when(userRepository.getByEmail(emailAddress)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> command.execute(),
            "The command should throw EntityNotFoundException when the user is not found.");
        verify(userRepository).getByEmail(emailAddress);
    }

    @Test
    void testExecuteThrowsIllegalArgumentExceptionWhenPasswordIsInvalid() {
        User mockUser = mock(User.class);
        when(userRepository.getByEmail(emailAddress)).thenReturn(Optional.of(mockUser));
        when(hashingService.strongHash(password)).thenReturn(hashedPassword);
        when(mockUser.comparePassword(hashedPassword)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> command.execute(),
            "The command should throw IllegalArgumentException when the password is invalid.");
        verify(userRepository).getByEmail(emailAddress);
        verify(hashingService).strongHash(password);
        verify(mockUser).comparePassword(hashedPassword);
    }
}