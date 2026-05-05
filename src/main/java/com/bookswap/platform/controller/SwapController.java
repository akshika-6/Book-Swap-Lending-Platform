package com.bookswap.platform.controller;

import com.bookswap.platform.dto.SwapRequestDto;
import com.bookswap.platform.model.SwapRequest;
import com.bookswap.platform.model.SwapStatus;
import com.bookswap.platform.service.SwapService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/swaps")
@RequiredArgsConstructor
public class SwapController {

    private final SwapService swapService;

    @PostMapping
    public ResponseEntity<SwapRequest> createSwapRequest(@Valid @RequestBody SwapRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(swapService.createSwapRequest(request));
    }

    @PatchMapping("/{swapRequestId}/status")
    public ResponseEntity<SwapRequest> updateSwapStatus(@PathVariable Long swapRequestId,
                                                        @RequestParam SwapStatus status) {
        return ResponseEntity.ok(swapService.updateStatus(swapRequestId, status));
    }
}
