package com.epam.finaltask.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard API response wrapper")
public record ApiResponse<T>(

        @Schema(description = "Response status code", example = "OK")
        String statusCode,

        @Schema(description = "Response message", example = "Success")
        String statusMessage,

        @Schema(description = "Payload of the response")
        T results
) {

    public static <T> ApiResponse<T> ok(String message, T results) {
        return new ApiResponse<>("OK", message, results);
    }

    public static <T> ApiResponse<T> ok(T results) {
        return new ApiResponse<>("OK", null, results);
    }
}
