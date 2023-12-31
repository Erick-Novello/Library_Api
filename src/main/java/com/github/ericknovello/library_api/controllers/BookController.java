package com.github.ericknovello.library_api.controllers;

import com.github.ericknovello.library_api.dtos.BookDto;
import com.github.ericknovello.library_api.entities.Book;
import com.github.ericknovello.library_api.exception.ApiErrors;
import com.github.ericknovello.library_api.exception.BusinessException;
import com.github.ericknovello.library_api.services.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/books")
public class BookController {

    @Autowired
    BookService bookService;

    private final ModelMapper modelMapper;

    @Autowired
    public BookController() {
        this.modelMapper = new ModelMapper();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto create(@Valid @RequestBody BookDto bookDto) {
        Book toBook = modelMapper.map(bookDto, Book.class);
        Book bookSaved = bookService.save(toBook);
        return modelMapper.map(bookSaved, BookDto.class);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrors handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return new ApiErrors(bindingResult);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    public ApiErrors handlerBussinessErrors(BusinessException ex) {
        return new ApiErrors(ex);
    }

}
