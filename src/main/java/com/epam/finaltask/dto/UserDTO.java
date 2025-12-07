package com.epam.finaltask.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "User DTO for API responses")
public record UserDTO(

        @Schema(description = "User ID", example = "d1e8c3bd-4d52-4e91-bcdb-91b25a8d343b")
        String id,

        @Schema(description = "Username", example = "john123")
        String username,

        @Schema(description = "Password hash", example = "******")
        String password,

        @Schema(description = "User role", example = "ADMIN")
        String role,

        @Schema(description = "List of voucher IDs owned by the user")
        List<String> voucherIds,

        @Schema(description = "Phone number", example = "+998901234567")
        String phoneNumber,

        @Schema(description = "User balance", example = "150.50")
        Double balance,

        @Schema(description = "Active status", example = "true")
        boolean active

) {

}
