package org.abc_berkut_test_task.controller;

import lombok.RequiredArgsConstructor;
import org.abc_berkut_test_task.service.BotService;
import org.abc_berkut_test_task.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/telegram")
@RequiredArgsConstructor
public class TelegramController {
    private final UserService userService;
    private final BotService telegramService;

    @GetMapping("/generate-token")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> generateToken(Principal principal) {
        try {
            String token = userService.generateTelegramToken(principal.getName());
            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "message", "Send this token to your Telegram bot"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhook(@RequestBody Update update) {
        telegramService.processTelegramUpdate(update);
        return ResponseEntity.ok().build();
    }
}
