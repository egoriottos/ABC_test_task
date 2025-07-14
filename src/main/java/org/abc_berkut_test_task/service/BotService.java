package org.abc_berkut_test_task.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.abc_berkut_test_task.config.BotProperties;
import org.abc_berkut_test_task.entity.Message;
import org.abc_berkut_test_task.entity.TelegramToken;
import org.abc_berkut_test_task.entity.User;
import org.abc_berkut_test_task.repository.MessageRepository;
import org.abc_berkut_test_task.repository.TelegramTokenRepository;
import org.abc_berkut_test_task.repository.UserRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BotService {
    private final TelegramTokenRepository telegramTokenRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final BotProperties botProperties;

    public void sendMessage(Long chatId, String text) {
        try {
            String url = "https://api.telegram.org/bot" + botProperties.getBotToken() + "/sendMessage";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("chat_id", chatId);
            requestBody.put("text", text);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForObject(url, entity, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send Telegram message", e);
        }
    }

    public void processTelegramUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            Optional<TelegramToken> tokenOpt = telegramTokenRepository.findByToken(text);
            if (tokenOpt.isPresent()) {
                TelegramToken token = tokenOpt.get();
                token.setChatId(chatId);
                telegramTokenRepository.save(token);

                sendMessage(chatId, "Token successfully linked to your chat!");
            }
        }
    }

    public void sendUserMessage(String username, String messageText) {
        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        TelegramToken token = telegramTokenRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Telegram token not found"));

        if (token.getChatId() == null) {
            log.error("Telegram chat not linked");
        }

        String formattedMessage = String.format("%s, я получил от тебя сообщение:\n%s",
                user.getName(), messageText);

        sendMessage(token.getChatId(), formattedMessage);

        Message message = Message.builder()
                .user(user)
                .messageText(messageText)
                .createdAt(LocalDateTime.now())
                .build();
        messageRepository.save(message);
    }

}
