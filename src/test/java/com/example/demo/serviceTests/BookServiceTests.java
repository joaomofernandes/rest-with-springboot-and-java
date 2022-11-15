package com.example.demo.serviceTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.example.demo.Exceptions.RequiredObjectIsNullException;
import com.example.demo.MockBook;
import com.example.demo.Model.Book;
import com.example.demo.Repository.BookRepository;
import com.example.demo.Service.BookService;
import com.example.demo.data.vo.v1.BookVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServiceTests {

    MockBook input;

    @InjectMocks
    private BookService service;

    @Mock
    BookRepository repository;

    @BeforeEach
    void setUpMocks() throws Exception {
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() throws ParseException {
        Random rand = new Random();
        Long number = Long.valueOf(rand.nextInt(10));
        Book entity = input.mockEntity(number);
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        var result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Title test " + number, result.getTitle());
        assertEquals("Author test " + number, result.getAuthor());
        String date = "2010-01-01";
        Date releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        assertEquals(releaseDate, result.getReleaseDate());
        assertEquals("Test Genre "+ + number, result.getGenre());
    }

    @Test
    void testCreate() throws ParseException {
        Book entity = input.mockEntity(1L);
        entity.setId(1L);

        Book persisted = entity;
        persisted.setId(1L);

        BookVO vo = input.mockVO(1L);
        vo.setId(1L);

        when(repository.save(entity)).thenReturn(entity);

        var result = service.create(vo);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Title test " + 1L, result.getTitle());
        assertEquals("Author test " + 1L, result.getAuthor());
        String date = "2010-01-01";
        Date releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        assertEquals(releaseDate, result.getReleaseDate());
        assertEquals("Test Genre " + 1L, result.getGenre());
    }

    @Test
    void testCreateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.create(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void testUpdate() throws ParseException {
        Book entity = input.mockEntity(1L);

        Book persisted = entity;
        persisted.setId(1L);

        BookVO vo = input.mockVO(1L);
        vo.setId(1L);


        /*when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);*/

        var result = service.update(vo);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Title test " + 1L, result.getTitle());
        assertEquals("Author test " + 1L, result.getAuthor());
        String date = "2010-01-01";
        Date releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        assertEquals(releaseDate, result.getReleaseDate());
        assertEquals("Test Genre " + 1L, result.getGenre());
    }



    @Test
    void testUpdateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.update(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDelete() throws ParseException {
        Book entity = input.mockEntity(1L);
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);
    }
   /*
    @Test
    void testFindAll() throws ParseException {
        List<Book> list = input.mockEntityList();

        when(repository.findAll()).thenReturn(list);

        var people = service.findAll(pageable);

        assertNotNull(people);
        assertEquals(14, people.size());

        var bookOne = people.get(1);

        assertNotNull(bookOne);
        assertNotNull(bookOne.getId());
        assertNotNull(bookOne.getLinks());

        String date = "2010-01-01";
        Date releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);

        assertTrue(bookOne.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Title test " + 1, bookOne.getTitle());
        assertEquals("Author test " + 1, bookOne.getAuthor());
        assertEquals(releaseDate, bookOne.getReleaseDate());
        assertEquals("Test Genre " + 1, bookOne.getGenre());

        var bookFour = people.get(4);

        assertNotNull(bookFour);
        assertNotNull(bookFour.getId());
        assertNotNull(bookFour.getLinks());

        assertTrue(bookFour.toString().contains("links: [</api/book/v1/4>;rel=\"self\"]"));
        assertEquals("Title test " + 4, bookFour.getTitle());
        assertEquals("Author test " + 4, bookFour.getAuthor());
        assertEquals(releaseDate, bookFour.getReleaseDate());
        assertEquals("Test Genre "+ + 4, bookFour.getGenre());

        var bookSeven = people.get(7);

        assertNotNull(bookSeven);
        assertNotNull(bookSeven.getId());
        assertNotNull(bookSeven.getLinks());

        assertTrue(bookSeven.toString().contains("links: [</api/book/v1/7>;rel=\"self\"]"));
        assertEquals("Title test " + 7, bookSeven.getTitle());
        assertEquals("Author test " + 7, bookSeven.getAuthor());
        assertEquals(releaseDate, bookSeven.getReleaseDate());
        assertEquals("Test Genre "+ + 7, bookSeven.getGenre());

    }*/

}
