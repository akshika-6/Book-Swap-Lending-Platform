package com.bookswap.platform.controller;

import com.bookswap.platform.dto.RequestCreateDto;
import com.bookswap.platform.model.Request;
import com.bookswap.platform.model.RequestStatus;
import com.bookswap.platform.service.RequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<Request> sendRequest(@Valid @RequestBody RequestCreateDto request) {
        Request createdRequest = request.type() == com.bookswap.platform.model.RequestType.SWAP
                ? requestService.sendSwapRequest(request)
                : requestService.sendBorrowRequest(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Request> approveRequest(@PathVariable Long id) {
        return ResponseEntity.ok(requestService.approveRequest(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Request> rejectRequest(@PathVariable Long id) {
        return ResponseEntity.ok(requestService.rejectRequest(id));
    }

    @PutMapping("/{id}/return")
    public ResponseEntity<Request> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(requestService.returnBook(id));
    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<Request> updateStatus(@PathVariable Long id, @PathVariable RequestStatus status) {
        return ResponseEntity.ok(requestService.updateRequestStatus(id, status));
    }
}