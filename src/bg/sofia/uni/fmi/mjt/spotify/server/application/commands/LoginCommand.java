package bg.sofia.uni.fmi.mjt.spotify.server.application.commands;

import bg.sofia.uni.fmi.mjt.spotify.server.application.HashingService;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.exception.EntityNotFoundException;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.models.User;
import bg.sofia.uni.fmi.mjt.spotify.server.domain.repositories.UserRepository;

public class LoginCommand implements Command {
    private final UserRepository userRepository;
    private final String emailAddress;
    private final String password;
    private final HashingService hashingService;

    public LoginCommand(UserRepository userRepository, HashingService hashingService, String emailAddress,
                        String password) {
        this.userRepository = userRepository;
        this.emailAddress = emailAddress;
        this.hashingService = hashingService;
        this.password = password;
    }

    @Override
    public Object execute() {
        User user = userRepository.getByEmail(emailAddress)
            .orElseThrow(() -> new EntityNotFoundException("No user with email address" + emailAddress));

        String hashedPassword = hashingService.strongHash(password);
        if (!user.comparePassword(hashedPassword)) {
            throw new IllegalArgumentException("Invalid password");
        }
        return null;
    }
}
