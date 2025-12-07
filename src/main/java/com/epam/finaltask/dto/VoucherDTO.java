package com.epam.finaltask.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Voucher DTO representing travel package information")
public record VoucherDTO(

        @Schema(description = "Voucher ID", example = "e73d9092-4315-4a0e-bda8-6fd27a3e54f6")
        String id,

        @Schema(description = "Voucher title", example = "Summer Sale")
        String title,

        @Schema(description = "Detailed description of the voucher", example = "All-inclusive holiday package")
        String description,

        @Schema(description = "Price of the voucher", example = "299.99")
        Double price,

        @Schema(description = "Tour type", example = "ADVENTURE")
        String tourType,

        @Schema(description = "Transport type", example = "PLANE")
        String transferType,

        @Schema(description = "Hotel type", example = "FIVE_STARS")
        String hotelType,

        @Schema(description = "Voucher status", example = "PAID")
        String status,

        @Schema(description = "Arrival date")
        LocalDate arrivalDate,

        @Schema(description = "Eviction or return date")
        LocalDate evictionDate,

        @Schema(description = "ID of the user who owns the voucher")
        UUID userId,

        @Schema(description = "Indicates whether voucher is marked as hot", example = "true")
        Boolean isHot
) {}
