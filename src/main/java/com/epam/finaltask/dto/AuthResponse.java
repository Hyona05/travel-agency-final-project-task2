package com.epam.finaltask.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication response containing tokens and user data")
public record AuthResponse(

        @Schema(description = "JWT access token")
        String accessToken,

        @Schema(description = "JWT refresh token")
        String refreshToken,

        @Schema(description = "Authenticated user information")
        UserDTO user
) {}
