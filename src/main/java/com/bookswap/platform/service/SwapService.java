package com.bookswap.platform.service;

import com.bookswap.platform.dto.SwapRequestDto;
import com.bookswap.platform.model.SwapRequest;
import com.bookswap.platform.model.SwapStatus;

public interface SwapService {
    SwapRequest createSwapRequest(SwapRequestDto request);

    SwapRequest updateStatus(Long swapRequestId, SwapStatus status);
}
