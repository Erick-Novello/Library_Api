package com.github.ericknovello.library_api.services;

import com.github.ericknovello.library_api.entities.Book;

import java.util.Optional;

public interface BookService {

    Book save(Book book);

    Optional<Book> getById(Long id);

    void deleteBook(Book book);

    Book update(Book book);

    Optional<Book> getBookByIsbn(String isbn);

}
