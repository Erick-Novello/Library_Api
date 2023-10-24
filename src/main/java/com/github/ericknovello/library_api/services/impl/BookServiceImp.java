package com.github.ericknovello.library_api.services.impl;

import com.github.ericknovello.library_api.entities.Book;
import com.github.ericknovello.library_api.exception.BusinessException;
import com.github.ericknovello.library_api.repositories.BookRepository;
import com.github.ericknovello.library_api.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookServiceImp implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        if (bookRepository.existsByIsbn(book.getIsbn())) {
            throw new BusinessException("ISBN ja cadastrado");
        }
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> getById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public void deleteBook(Book book) {
        if (book == null || book.getId() == null) {
            throw new IllegalArgumentException("Book nao pode ser nulo");
        }
        bookRepository.delete(book);
    }

    @Override
    public Book update(Book book) {
        if (book == null || book.getId() == null) {
            throw new IllegalArgumentException("Book nao pode ser nulo");
        }
        return bookRepository.save(book);
    }

    @Override
    public Optional<Book> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }
}
