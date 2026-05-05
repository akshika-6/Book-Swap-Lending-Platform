package com.bookswap.platform.service;

import com.bookswap.platform.dto.RequestCreateDto;
import com.bookswap.platform.model.Request;
import com.bookswap.platform.model.RequestStatus;
import java.util.List;

public interface RequestService {
    Request sendBorrowRequest(RequestCreateDto request);

    Request sendSwapRequest(RequestCreateDto request);

    Request approveRequest(Long requestId);

    Request rejectRequest(Long requestId);

    Request returnBook(Long requestId);

    Request updateRequestStatus(Long requestId, RequestStatus status);

    List<Request> getRequestsByRequester(Long requesterId);

    List<Request> getRequestsByOwner(Long ownerId);

    List<Request> getRequestsByStatus(RequestStatus status);
}