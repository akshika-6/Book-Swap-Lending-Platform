package com.bookswap.platform.service.impl;

import com.bookswap.platform.dto.BookRequest;
import com.bookswap.platform.exception.BusinessException;
import com.bookswap.platform.exception.ResourceNotFoundException;
import com.bookswap.platform.model.AppUser;
import com.bookswap.platform.model.Book;
import com.bookswap.platform.repository.BookRepository;
import com.bookswap.platform.repository.UserRepository;
import com.bookswap.platform.service.BookService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Book addBook(BookRequest request) {
        AppUser owner = userRepository.findById(request.ownerId())
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        Book book = Book.builder()
                .title(request.title())
                .author(request.author())
                .isbn(request.isbn())
                .genre(request.genre())
                .location(request.location())
                .condition(request.condition())
                .owner(owner)
                .available(true)
                .build();

        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book updateBook(Long id, BookRequest request) {
        Book book = getById(id);

        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setIsbn(request.isbn());
        book.setGenre(request.genre());
        book.setLocation(request.location());
        book.setCondition(request.condition());

        return book;
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        Book book = getById(id);
        bookRepository.delete(book);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> getAvailableBooks() {
        return bookRepository.findByAvailableTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Book getById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> searchBooks(String title, String author, String genre, String location) {
        List<Book> books = new java.util.ArrayList<>();

        if (title != null && !title.isBlank()) {
            books.addAll(bookRepository.findByTitleContainingIgnoreCase(title));
        }
        if (author != null && !author.isBlank()) {
            books.addAll(bookRepository.findByAuthorContainingIgnoreCase(author));
        }
        if (genre != null && !genre.isBlank()) {
            books.addAll(bookRepository.findByGenreContainingIgnoreCase(genre));
        }
        if (location != null && !location.isBlank()) {
            books.addAll(bookRepository.findByLocationContainingIgnoreCase(location));
        }

        if (books.isEmpty()) {
            return getAllBooks();
        }

        return books.stream()
                .collect(java.util.stream.Collectors.toMap(Book::getId, book -> book, (left, right) -> left, java.util.LinkedHashMap::new))
                .values()
                .stream()
                .toList();
    }
}
