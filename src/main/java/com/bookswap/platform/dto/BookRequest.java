package com.bookswap.platform.dto;

import com.bookswap.platform.model.BookCondition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BookRequest(
        @NotBlank @Size(max = 200) String title,
        @NotBlank @Size(max = 150) String author,
        @NotBlank @Size(max = 20) String isbn,
        @Size(max = 100) String genre,
        @Size(max = 100) String location,
        @NotNull BookCondition condition,
        @NotNull Long ownerId
) {
}
