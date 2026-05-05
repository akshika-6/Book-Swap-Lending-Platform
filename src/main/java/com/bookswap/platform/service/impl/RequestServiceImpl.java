package com.bookswap.platform.service.impl;

import com.bookswap.platform.dto.RequestCreateDto;
import com.bookswap.platform.exception.BusinessException;
import com.bookswap.platform.exception.ResourceNotFoundException;
import com.bookswap.platform.model.AppUser;
import com.bookswap.platform.model.Book;
import com.bookswap.platform.model.Request;
import com.bookswap.platform.model.RequestStatus;
import com.bookswap.platform.model.RequestType;
import com.bookswap.platform.repository.BookRepository;
import com.bookswap.platform.repository.RequestRepository;
import com.bookswap.platform.repository.UserRepository;
import com.bookswap.platform.service.RequestService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Request sendBorrowRequest(RequestCreateDto request) {
        if (request.type() != RequestType.BORROW) {
            throw new BusinessException("Request type must be BORROW");
        }

        Book requestedBook = loadBook(request.requestedBookId());
        AppUser requester = loadUser(request.requesterId());
        AppUser owner = loadUser(request.ownerId());

        validateOwnership(requestedBook, owner);
        validateNotSelfRequest(requester, owner);
        ensureAvailableForBorrow(requestedBook);

        Request savedRequest = Request.builder()
                .requester(requester)
                .owner(owner)
                .requestedBook(requestedBook)
                .type(RequestType.BORROW)
                .status(RequestStatus.PENDING)
                .message(request.message())
                .build();

        return requestRepository.save(savedRequest);
    }

    @Override
    @Transactional
    public Request sendSwapRequest(RequestCreateDto request) {
        if (request.type() != RequestType.SWAP) {
            throw new BusinessException("Request type must be SWAP");
        }

        if (request.offeredBookId() == null) {
            throw new BusinessException("Offered book is required for swap requests");
        }

        Book requestedBook = loadBook(request.requestedBookId());
        Book offeredBook = loadBook(request.offeredBookId());
        AppUser requester = loadUser(request.requesterId());
        AppUser owner = loadUser(request.ownerId());

        validateOwnership(requestedBook, owner);
        validateOwnership(offeredBook, requester);
        validateNotSelfRequest(requester, owner);

        Request savedRequest = Request.builder()
                .requester(requester)
                .owner(owner)
                .requestedBook(requestedBook)
                .offeredBook(offeredBook)
                .type(RequestType.SWAP)
                .status(RequestStatus.PENDING)
                .message(request.message())
                .build();

        return requestRepository.save(savedRequest);
    }

    @Override
    @Transactional
    public Request approveRequest(Long requestId) {
        Request request = loadRequest(requestId);

        if (request.getType() == RequestType.BORROW) {
            ensureAvailableForBorrow(request.getRequestedBook());
            request.getRequestedBook().setAvailable(false);
        } else {
            swapBooks(request);
        }

        request.setStatus(RequestStatus.APPROVED);
        return request;
    }

    @Override
    @Transactional
    public Request rejectRequest(Long requestId) {
        Request request = loadRequest(requestId);
        request.setStatus(RequestStatus.REJECTED);
        return request;
    }

    @Override
    @Transactional
    public Request returnBook(Long requestId) {
        Request request = loadRequest(requestId);

        if (request.getType() != RequestType.BORROW) {
            throw new BusinessException("Only borrow requests can be returned");
        }

        if (request.getRequestedBook().isAvailable()) {
            throw new BusinessException("Book is already marked as available");
        }

        request.getRequestedBook().setAvailable(true);
        request.setStatus(RequestStatus.RETURNED);
        return request;
    }

    @Override
    @Transactional
    public Request updateRequestStatus(Long requestId, RequestStatus status) {
        Request request = loadRequest(requestId);
        request.setStatus(status);
        return request;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Request> getRequestsByRequester(Long requesterId) {
        return requestRepository.findByRequester(loadUser(requesterId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Request> getRequestsByOwner(Long ownerId) {
        return requestRepository.findByOwner(loadUser(ownerId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Request> getRequestsByStatus(RequestStatus status) {
        return requestRepository.findByStatus(status);
    }

    private void swapBooks(Request request) {
        Book requestedBook = request.getRequestedBook();
        Book offeredBook = request.getOfferedBook();

        if (offeredBook == null) {
            throw new BusinessException("Offered book is required for swap approval");
        }

        AppUser owner = request.getOwner();
        AppUser requester = request.getRequester();

        requestedBook.setOwner(requester);
        offeredBook.setOwner(owner);
        requestedBook.setAvailable(true);
        offeredBook.setAvailable(true);
    }

    private void validateOwnership(Book book, AppUser owner) {
        if (!book.getOwner().getId().equals(owner.getId())) {
            throw new BusinessException("Book does not belong to the expected user");
        }
    }

    private void validateNotSelfRequest(AppUser requester, AppUser owner) {
        if (requester.getId().equals(owner.getId())) {
            throw new BusinessException("Requester and owner must be different users");
        }
    }

    private void ensureAvailableForBorrow(Book book) {
        if (!book.isAvailable()) {
            throw new BusinessException("Book is already borrowed");
        }
    }

    private Request loadRequest(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
    }

    private Book loadBook(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    private AppUser loadUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}