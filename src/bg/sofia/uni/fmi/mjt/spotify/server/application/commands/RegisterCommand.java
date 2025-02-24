package bg.sofia.uni.fmi.mjt.spotify.server.application.commands;

import bg.sofia.uni.fmi.mjt.spotify.server.application.HashingService;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.User;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.UserRepository;

import java.util.Optional;

public class RegisterCommand implements Command {
    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final String emailAddress;
    private final String password;

    public RegisterCommand(UserRepository userRepository, HashingService hashingService, String emailAddress,
                           String password) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.emailAddress = emailAddress;
        this.password = password;
    }

    @Override
    public User execute() {
        Optional<User> existingUser = userRepository.getByEmail(emailAddress);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("User with email" + emailAddress + " already present");
        }

        String passwordHash = hashingService.strongHash(password);
        User user = new User(null, emailAddress, passwordHash);

        return userRepository.create(user);
    }
}
