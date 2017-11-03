package com.springboot.repository;

import com.springboot.entity.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends CrudRepository<Book, Long> {

    List<Book> findByTitle(String title);
    Optional<Book> findById(long id);
}
