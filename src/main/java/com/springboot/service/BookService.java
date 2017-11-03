package com.springboot.service;

import com.springboot.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> findByTitle(String title);
    Optional<Book> findBook(long id);
    void deleteBook(long id);
    Book saveBook(Book book);
    Book updateBook(Book book);
}
