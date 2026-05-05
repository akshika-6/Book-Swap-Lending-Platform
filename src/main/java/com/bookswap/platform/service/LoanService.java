package com.bookswap.platform.service;

import com.bookswap.platform.dto.LoanRequestDto;
import com.bookswap.platform.model.Loan;
import com.bookswap.platform.model.LoanStatus;

public interface LoanService {
    Loan createLoanRequest(LoanRequestDto request);

    Loan updateStatus(Long loanId, LoanStatus status);
}
