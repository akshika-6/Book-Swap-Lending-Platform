package com.bookswap.platform.controller;

import com.bookswap.platform.dto.LoanRequestDto;
import com.bookswap.platform.model.Loan;
import com.bookswap.platform.model.LoanStatus;
import com.bookswap.platform.service.LoanService;
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
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<Loan> createLoan(@Valid @RequestBody LoanRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loanService.createLoanRequest(request));
    }

    @PatchMapping("/{loanId}/status")
    public ResponseEntity<Loan> updateLoanStatus(@PathVariable Long loanId, @RequestParam LoanStatus status) {
        return ResponseEntity.ok(loanService.updateStatus(loanId, status));
    }
}
