package com.example.demo;

import com.example.demo.Model.Book;
import com.example.demo.Model.Person;
import com.example.demo.data.vo.v1.BookVO;
import com.example.demo.data.vo.v1.PersonVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockBook {

    public Book mockEntity(Long number) throws ParseException {
        Book book = new Book();
        book.setId(number.longValue());
        book.setTitle("Title test " + number);
        book.setAuthor("Author test " + number);
        String date = "2010-01-01";
        Date releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        book.setReleaseDate(releaseDate);
        book.setGenre("Test Genre " + number);

        return book;
    }

    public BookVO mockVO(Long number) throws ParseException {
        BookVO bookVO = new BookVO();
        bookVO.setId(number);
        bookVO.setTitle("Title test " + number);
        bookVO.setAuthor("Author test " + number);
        String date = "2010-01-01";
        Date releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        bookVO.setReleaseDate(releaseDate);
        bookVO.setGenre("Test Genre " + number);

        return bookVO;
    }

    public List<Book> mockEntityList() throws ParseException {
        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(Long.valueOf(i)));
        }
        return books;
    }
}
