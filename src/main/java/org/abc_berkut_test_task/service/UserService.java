package org.abc_berkut_test_task.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.abc_berkut_test_task.dto.RegisterRequest;
import org.abc_berkut_test_task.entity.TelegramToken;
import org.abc_berkut_test_task.entity.User;
import org.abc_berkut_test_task.repository.TelegramTokenRepository;
import org.abc_berkut_test_task.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper mapper;
    private final TelegramTokenRepository telegramTokenRepository;

    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByLogin(request.getLogin())) {
            log.error("User with login: {} already exist", request.getLogin());
        }
        User user = mapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    public User getUserByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public String generateTelegramToken(String login) {
        User user = getUserByLogin(login);

        telegramTokenRepository.findByUser(user)
                .ifPresent(telegramTokenRepository::delete);

        String token = UUID.randomUUID().toString();

        TelegramToken telegramToken = new TelegramToken();
        telegramToken.setToken(token);
        telegramToken.setUser(user);

        telegramTokenRepository.save(telegramToken);

        return token;
    }

}
