package com.bookswap.platform.service;

import com.bookswap.platform.dto.BookRequest;
import com.bookswap.platform.model.Book;
import java.util.List;

public interface BookService {
    Book addBook(BookRequest request);

    default Book createBook(BookRequest request) {
        return addBook(request);
    }

    Book updateBook(Long id, BookRequest request);

    void deleteBook(Long id);

    List<Book> getAllBooks();

    List<Book> getAvailableBooks();

    Book getById(Long id);

    List<Book> searchBooks(String title, String author, String genre, String location);
}
