package com.bookswap.platform.dto;

import com.bookswap.platform.model.RequestType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RequestCreateDto(
        @NotNull Long requesterId,
        @NotNull Long ownerId,
        @NotNull Long requestedBookId,
        Long offeredBookId,
        @NotNull RequestType type,
        @Size(max = 500) String message
) {
}