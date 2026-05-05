package com.bookswap.platform.service.impl;

import com.bookswap.platform.dto.SwapRequestDto;
import com.bookswap.platform.exception.BusinessException;
import com.bookswap.platform.exception.ResourceNotFoundException;
import com.bookswap.platform.model.AppUser;
import com.bookswap.platform.model.Book;
import com.bookswap.platform.model.SwapRequest;
import com.bookswap.platform.model.SwapStatus;
import com.bookswap.platform.repository.BookRepository;
import com.bookswap.platform.repository.SwapRequestRepository;
import com.bookswap.platform.repository.UserRepository;
import com.bookswap.platform.service.SwapService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SwapServiceImpl implements SwapService {

    private final SwapRequestRepository swapRequestRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public SwapRequest createSwapRequest(SwapRequestDto request) {
        if (request.requestedBookId().equals(request.offeredBookId())) {
            throw new BusinessException("Requested and offered books must be different");
        }

        Book requestedBook = bookRepository.findById(request.requestedBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Requested book not found"));

        Book offeredBook = bookRepository.findById(request.offeredBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Offered book not found"));

        AppUser requester = userRepository.findById(request.requesterId())
                .orElseThrow(() -> new ResourceNotFoundException("Requester not found"));

        SwapRequest swapRequest = SwapRequest.builder()
                .requestedBook(requestedBook)
                .offeredBook(offeredBook)
                .requester(requester)
                .message(request.message())
                .status(SwapStatus.REQUESTED)
                .build();

        return swapRequestRepository.save(swapRequest);
    }

    @Override
    @Transactional
    public SwapRequest updateStatus(Long swapRequestId, SwapStatus status) {
        SwapRequest swapRequest = swapRequestRepository.findById(swapRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Swap request not found"));
        swapRequest.setStatus(status);
        return swapRequest;
    }
}
