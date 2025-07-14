package org.abc_berkut_test_task.service;

import lombok.RequiredArgsConstructor;
import org.abc_berkut_test_task.entity.Message;
import org.abc_berkut_test_task.entity.User;
import org.abc_berkut_test_task.repository.MessageRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final BotService telegramService;
    private final MessageRepository messageRepository;

    public void sendMessage(String username, String messageText) {
        telegramService.sendUserMessage(username, messageText);
    }

    public List<Message> getUserMessages(User user) {
        return messageRepository.findByUserOrderByCreatedAtDesc(user);
    }
}
