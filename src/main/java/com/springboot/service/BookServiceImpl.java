package com.springboot.service;

import com.springboot.entity.Book;
import com.springboot.repository.BookRepository;
import com.springboot.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    @Override
    public Optional<Book> findBook(long id) {
        return bookRepository.findById(id);
    }

    @Override
    public void deleteBook(long id) {
        bookRepository.delete(id);
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Book book) {
        return saveBook(book);
    }
}
