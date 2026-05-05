package com.bookswap.platform.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SwapRequestDto(
        @NotNull Long requestedBookId,
        @NotNull Long offeredBookId,
        @NotNull Long requesterId,
        @Size(max = 500) String message
) {
}
