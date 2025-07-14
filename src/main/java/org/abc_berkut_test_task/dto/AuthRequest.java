package org.abc_berkut_test_task.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String login;
    private String password;
}
