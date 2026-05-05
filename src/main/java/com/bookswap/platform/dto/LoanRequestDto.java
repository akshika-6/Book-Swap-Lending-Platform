package com.bookswap.platform.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record LoanRequestDto(
        @NotNull Long bookId,
        @NotNull Long borrowerId,
        @NotNull @Future LocalDate requestedFrom,
        @NotNull @Future LocalDate requestedTo
) {
}
