package org.abc_berkut_test_task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDto {
    private String messageText;
    private Long id;
    private LocalDateTime createdAt;
}
