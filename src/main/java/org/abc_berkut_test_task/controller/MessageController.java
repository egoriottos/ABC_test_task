package org.abc_berkut_test_task.controller;

import lombok.RequiredArgsConstructor;
import org.abc_berkut_test_task.dto.MessageDto;
import org.abc_berkut_test_task.dto.MessageResponseDto;
import org.abc_berkut_test_task.entity.Message;
import org.abc_berkut_test_task.entity.User;
import org.abc_berkut_test_task.service.MessageService;
import org.abc_berkut_test_task.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDto messageDto, Principal principal) {
        try {
            messageService.sendMessage(principal.getName(), messageDto.getContent());
            return ResponseEntity.ok(Map.of(
                    "status", "Message sent successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserMessages(Principal principal) {
        try {
            User user = userService.getUserByLogin(principal.getName());
            List<Message> messages = messageService.getUserMessages(user);

            return ResponseEntity.ok(messages.stream()
                    .map(message -> new MessageResponseDto(
                            message.getMessageText(),
                            message.getId(),
                            message.getCreatedAt()
                    ))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }

}
