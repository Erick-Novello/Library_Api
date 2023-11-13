package com.github.ericknovello.library_api.services;

import com.github.ericknovello.library_api.entities.Book;
import com.github.ericknovello.library_api.exception.BusinessException;
import com.github.ericknovello.library_api.repositories.BookRepository;
import com.github.ericknovello.library_api.services.impl.BookServiceImp;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class BookServiceTest {

    BookService bookService;

    @MockBean
    BookRepository bookRepository;

    @BeforeEach
    public void setup() {
        this.bookService = new BookServiceImp(bookRepository);
    }

    @Test
    @DisplayName("Deve salvar um livro")
    public void saveBookTest() {
        //cenario
        Long id = 1L;
        Book bookRequest = builderBook(id);
        Book bookResponse = builderBook(id);

        Mockito.when(bookRepository.existsByIsbn(Mockito.anyString())).thenReturn(false);
        Mockito.when(bookRepository.save(bookRequest)).thenReturn(bookResponse);

        //execuçao
        Book bookSaved = bookService.save(bookResponse);

        //validaçao
        Assertions.assertThat(bookSaved.getId()).isNotNull();
        Assertions.assertThat(bookSaved.getId()).isEqualTo(1L);
        Assertions.assertThat(bookSaved.getTitle()).isEqualTo(bookRequest.getTitle());
        Assertions.assertThat(bookSaved.getAutor()).isEqualTo(bookRequest.getAutor());
        Assertions.assertThat(bookSaved.getIsbn()).isEqualTo(bookRequest.getIsbn());
    }

    @Test
    @DisplayName("Deve lançar lançar um erro de negocio ao tentar salvar um livro com ISBN duplicado")
    public void shouldNotSaveABookWithDuplicatedIsbn() {
        //cenario
        Book bookReceived = builderBook(null);
        Mockito.when(bookRepository.existsByIsbn(Mockito.anyString())).thenReturn(true);

        //execuçao
        Throwable exception = Assertions.catchThrowable(() -> bookService.save(bookReceived));

        //validaçao
        Assertions.assertThat(exception).isInstanceOf(BusinessException.class).hasMessage("ISBN ja cadastrado");
        Mockito.verify(bookRepository, Mockito.never()).save(bookReceived);

    }

    @Test
    @DisplayName("Deve obter um livro por ID")
    public void getBookById() {
        //cenario
        Long id = 1L;
        Book book = builderBook(id);
        book.setId(id);

        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        //execuçao
        Optional<Book> foundBook = bookService.getById(id);

        //validaçao
        Assertions.assertThat(foundBook.isPresent()).isTrue();
        Assertions.assertThat(foundBook.get().getId()).isEqualTo(id);
        Assertions.assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
        Assertions.assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
        Assertions.assertThat(foundBook.get().getAutor()).isEqualTo(book.getAutor());
    }

    @Test
    @DisplayName("Deve retornar vazio ao obter um livro por Id")
    public void bookNotFoundByIdTest() {
        //cenario
        Long id = 1L;
        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.empty());

        //execuçao
        Optional<Book> book = bookService.getById(id);

        //validaçao
        Assertions.assertThat(book.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Deve ocorrer um erro ao tentar deletar um livro inexistente")
    public void deleteInvalidaBookTest() {
        Book book = new Book();

        org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> bookService.deleteBook(book));

        Mockito.verify(bookRepository, Mockito.never()).delete(book);
    }

    @Test
    @DisplayName("Deve atualizar um livro")
    public void updateBookTest() {
        Long id = 1L;
        Book bookReceived = Book.builder().id(1L).build();
        Book bookReturned = builderBook(id);
        bookReturned.setId(id);

        Mockito.when(bookRepository.save(bookReceived)).thenReturn(bookReturned);

        Book bookUpdated = bookService.update(bookReceived);

        Assertions.assertThat(bookUpdated.getId()).isEqualTo(bookReturned.getId());
        Assertions.assertThat(bookUpdated.getIsbn()).isEqualTo(bookReturned.getIsbn());
        Assertions.assertThat(bookUpdated.getAutor()).isEqualTo(bookReturned.getAutor());
        Assertions.assertThat(bookUpdated.getTitle()).isEqualTo(bookReturned.getTitle());
    }

    @Test
    @DisplayName("Deve ocorrer um erro ao tentar deletar um livro inexistente")
    public void updateInvalidBookTest() {
        Book book = new Book();

        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> bookService.update(book));

        Mockito.verify(bookRepository, Mockito.never()).delete(book);
    }

    @Test
    @DisplayName("Deve obter um livro por ISBN")
    public void getBookByIsbn() {
        String isbn = "1231324512";
        Long id = 1L;
        Book bookReturned = Book.builder().id(id).isbn(isbn).build();
        Mockito.when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(bookReturned));

        Optional<Book> bookFound = bookService.getBookByIsbn(isbn);

        Assertions.assertThat(bookFound.isPresent()).isTrue();
        Assertions.assertThat(bookFound.get().getId()).isEqualTo(id);
        Assertions.assertThat(bookFound.get().getIsbn()).isEqualTo(isbn);

        Mockito.verify(bookRepository, Mockito.times(1)).findByIsbn(isbn);
    }

    private Book builderBook(Long id) {
        return Book.builder()
                .id(id)
                .title("Titulo")
                .autor("Erick")
                .isbn("15854615")
                .build();
    }
}
