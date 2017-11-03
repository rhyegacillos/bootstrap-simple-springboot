package com.springboot.test.controller;

import com.springboot.SpringbootApplication;
import com.springboot.entity.Book;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BookControllerTest {
    private static final String API_ROOT = "http://localhost:8080/api/books";

    @Before
    public void setup() {
        RestAssured.authentication = preemptive().basic("rhye","rhye");
    }

    private Book createRandomBook() {
        final Book book = new Book();
        book.setTitle(randomAlphabetic(10));
        book.setAuthor(randomAlphabetic(15));
        return book;
    }

    private String createBookAsUri(Book book) {
        final Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(book)
                .post(API_ROOT);
        return API_ROOT + "/" + response.jsonPath()
                .get("id");
    }

    // find books

    @Test
    public void whenGetAllBooks_thenOK() {
        Response response = RestAssured.get(API_ROOT);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void whenGetBooksByTitle_thenOK() {
        Book book = createRandomBook();
        createBookAsUri(book);
        Response response = RestAssured.get(API_ROOT + "/title/" + book.getTitle());

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertTrue(response.as(List.class).size() > 0);
    }

    @Test
    public void whenGetCreatedBookById_thenOK() {
        Book book = createRandomBook();
        String location = createBookAsUri(book);
        Response response = RestAssured.get(location);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(book.getTitle(), response.jsonPath().get("title"));
    }

    @Test
    public void whenGetNotExistBookId_thenNotFound() {
        Response response = get(API_ROOT + "/" + randomNumeric(4));

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }

    // creating new book
    @Test
    public void whenCreateNewBook_theCreated() {
        Book book = createRandomBook();
        Response response = given().contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(book)
                            .post(API_ROOT);

        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }

    @Test
    public void whenInvalidBook_thenError() {
        Book book = createRandomBook();
        Response response = given().contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(book)
                            .post(API_ROOT);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    //update an existing book
    @Test
    public void whenUpdateCreatedBook_thenUpdated() {
        final Book book = createRandomBook();
        final String location = createBookAsUri(book);
        book.setId(Long.parseLong(location.split("api/books/")[1]));
        book.setAuthor("newAuthor");
        Response response = given().contentType(MediaType.APPLICATION_JSON_VALUE)
                                .body(book)
                                .put(location);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        response = get(location);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals("newAuthor", response.jsonPath().get("author"));
    }

    @Test
    public void whenDeleteCreatedBook_thenOk() {
        Book book = createRandomBook();
        String location = createBookAsUri(book);
        Response response = delete(location);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        response = get(location);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }

}
