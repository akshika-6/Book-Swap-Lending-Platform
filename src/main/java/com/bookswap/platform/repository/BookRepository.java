package com.bookswap.platform.repository;

import com.bookswap.platform.model.Book;
import com.bookswap.platform.model.AppUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAvailableTrue();

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorContainingIgnoreCase(String author);

    List<Book> findByGenreContainingIgnoreCase(String genre);

    List<Book> findByLocationContainingIgnoreCase(String location);

    List<Book> findByOwner(AppUser owner);
}
