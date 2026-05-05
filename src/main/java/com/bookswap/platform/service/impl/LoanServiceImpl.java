package com.bookswap.platform.service.impl;

import com.bookswap.platform.dto.LoanRequestDto;
import com.bookswap.platform.exception.BusinessException;
import com.bookswap.platform.exception.ResourceNotFoundException;
import com.bookswap.platform.model.AppUser;
import com.bookswap.platform.model.Book;
import com.bookswap.platform.model.Loan;
import com.bookswap.platform.model.LoanStatus;
import com.bookswap.platform.repository.BookRepository;
import com.bookswap.platform.repository.LoanRepository;
import com.bookswap.platform.repository.UserRepository;
import com.bookswap.platform.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Loan createLoanRequest(LoanRequestDto request) {
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (!book.isAvailable()) {
            throw new BusinessException("Book is currently not available for lending");
        }

        AppUser borrower = userRepository.findById(request.borrowerId())
                .orElseThrow(() -> new ResourceNotFoundException("Borrower not found"));

        if (request.requestedFrom().isAfter(request.requestedTo())) {
            throw new BusinessException("Requested from date must be before requested to date");
        }

        Loan loan = Loan.builder()
                .book(book)
                .borrower(borrower)
                .requestedFrom(request.requestedFrom())
                .requestedTo(request.requestedTo())
                .status(LoanStatus.REQUESTED)
                .build();

        return loanRepository.save(loan);
    }

    @Override
    @Transactional
    public Loan updateStatus(Long loanId, LoanStatus status) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan request not found"));

        loan.setStatus(status);
        if (status == LoanStatus.APPROVED) {
            loan.getBook().setAvailable(false);
        }
        if (status == LoanStatus.RETURNED || status == LoanStatus.REJECTED) {
            loan.getBook().setAvailable(true);
        }

        return loan;
    }
}
