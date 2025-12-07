package com.epam.finaltask.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login request DTO")
public record LoginRequest(

        @Schema(description = "User's username", example = "john123")
        String username,

        @Schema(description = "User's password", example = "12345")
        String password
) {}
