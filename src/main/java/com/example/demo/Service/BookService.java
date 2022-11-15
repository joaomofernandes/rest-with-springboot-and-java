package com.example.demo.Service;

import com.example.demo.Controllers.v1.BookController;
import com.example.demo.Exceptions.RequiredObjectIsNullException;
import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Model.Book;
import com.example.demo.Repository.BookRepository;
import com.example.demo.data.vo.v1.BookVO;

import com.example.demo.data.vo.v1.PersonVO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class BookService {
    private Logger logger = Logger.getLogger(BookService.class.getName());

    private ModelMapper mapper = new ModelMapper();

    @Autowired
    BookRepository bookRepository;

    @Autowired
    PagedResourcesAssembler<BookVO> assembler;

    public BookVO findById(Long id){
        logger.info("Finding one book...");
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records have been found for this id!"));
        BookVO vo = new BookVO(book.getId(), book.getTitle(), book.getAuthor(), book.getReleaseDate(), book.getGenre());
        BookVO book2 = vo;
        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return vo;
    }
    public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable){

        Page booksPage = bookRepository.findAll(pageable);

        Page bookVOsPage = booksPage.map((Function<Book, BookVO>) b -> new ModelMapper().map(b, BookVO.class)
                .add(linkTo(methodOn(BookController.class).findById(b.getId())).withSelfRel()));
        Link link = linkTo(methodOn(BookController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(bookVOsPage, link);
    }

    public BookVO create(BookVO book){
        if(book ==  null){
            throw new RequiredObjectIsNullException("It is not allowed to persist a null object!");
        }
        logger.info("Creating a new book...");
        Book bookToSave = mapper.map(book, Book.class);
        bookRepository.save(bookToSave);
        book.add(linkTo(methodOn(BookController.class).findById(book.getId())).withSelfRel());
        return book;
    }
    public BookVO update(BookVO book){
        if(book ==  null){
            throw new RequiredObjectIsNullException("It is not allowed to persist a null object!");
        }
        logger.info("Updating a book...");
        Date date = new Date();

        Book bookToEdit = mapper.map(book, Book.class);
        bookRepository.save(bookToEdit);
        book.add(linkTo(methodOn(BookController.class).findById(book.getId())).withSelfRel());
        return book;
    }

    public void delete(Long id){
        logger.info("Deleting a book...");
        Book entity = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records have been found for this id!"));
        bookRepository.delete(entity);
        logger.info("Book with id: " + id + " was deleted");
    }
}
