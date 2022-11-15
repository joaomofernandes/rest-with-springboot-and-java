package com.example.demo.integrationtests.controller.withxml;

import com.example.demo.configs.TestsConfigs;
import com.example.demo.integrationtests.testcontainers.AbstractIntegrationTests;
import com.example.demo.integrationtests.vo.AccountCredentialsVO;
import com.example.demo.integrationtests.vo.BookVO;
import com.example.demo.integrationtests.vo.TokenVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerXmlTests extends AbstractIntegrationTests {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static BookVO book;

    @BeforeAll
    public static void Setup(){
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        book = new BookVO();
    }

    @Test
    @Order(0)
    public void authorization() throws JsonMappingException{
        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
        var tokenVO = given()
                .basePath("/auth/signin")
                .port(TestsConfigs.SERVER_PORT)
                .contentType(TestsConfigs.CONTENT_TYPE_XML)
                .body(user)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body().as(TokenVO.class)
                .getAccessToken();

        specification = new RequestSpecBuilder().addHeader(TestsConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO)
                .setBasePath("/api/book/v1")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

    }

    @Test
    @Order(1)
    public void testCreate() throws JsonProcessingException, ParseException {
        mockBook();

        var content = given().spec(specification)
                        .contentType(TestsConfigs.CONTENT_TYPE_XML)
                        .header(TestsConfigs.HEADER_PARAM_ORIGIN, "http://localhost:8080")
                        .body(book)
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body().asString();
        BookVO createdBook = objectMapper.readValue(content, BookVO.class);
        book = createdBook;

        assertNotNull(createdBook);
        assertNotNull(createdBook.getId());
        assertNotNull(createdBook.getTitle());
        assertNotNull(createdBook.getAuthor());
        assertNotNull(createdBook.getReleaseDate());
        assertNotNull(createdBook.getGenre());

        assertTrue(createdBook.getId() > 0);

        assertEquals("Title test", createdBook.getTitle());
        assertEquals("Author test", createdBook.getAuthor());
        String date = "2010-01-01";
        Date releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        assertEquals(releaseDate, createdBook.getReleaseDate());
        assertEquals("Test Genre", createdBook.getGenre());
    }
    @Test
    @Order(2)
    public void testCreateWithWrongOrigin() throws JsonProcessingException, ParseException {
        mockBook();

        var content = given().spec(specification)
                .contentType(TestsConfigs.CONTENT_TYPE_XML)
                .header(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_SEMERU)
                .body(book)
                .when()
                .post()
                .then()
                .statusCode(403)
                .extract()
                .body().asString();

        assertNotNull(content);

        assertEquals("Invalid CORS request", content);
    }
    @Test
    @Order(3)
    public void findByID() throws JsonProcessingException, ParseException {
        mockBook();

        var content = given().spec(specification)
                .contentType(TestsConfigs.CONTENT_TYPE_XML)
                .header(TestsConfigs.HEADER_PARAM_ORIGIN, "http://localhost:8080")
                .pathParam("id", book.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body().asString();
        BookVO persistedBook = objectMapper.readValue(content, BookVO.class);
        book = persistedBook;

        assertNotNull(persistedBook);
        assertNotNull(persistedBook.getId());
        assertNotNull(persistedBook.getTitle());
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getReleaseDate());
        assertNotNull(persistedBook.getGenre());

        assertTrue(persistedBook.getId() > 0);

        assertEquals("Title test", persistedBook.getTitle());
        assertEquals("Author test", persistedBook.getAuthor());
        String date = "2010-01-01";
        Date releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        assertEquals(releaseDate, persistedBook.getReleaseDate());
        assertEquals("Test Genre", persistedBook.getGenre());
    }
    @Test
    @Order(4)
    public void findByIDWithWrongOrigin() throws JsonProcessingException, ParseException {
        mockBook();

        var content = given().spec(specification)
                .contentType(TestsConfigs.CONTENT_TYPE_XML)
                .header(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_SEMERU)
                .pathParam("id", book.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(403)
                .extract()
                .body().asString();

        assertNotNull(content);

        assertEquals("Invalid CORS request", content);
    }
    @Test
    @Order(5)
    public void findByIDNotFound() throws JsonProcessingException, ParseException {
        mockBook();

        var content = given().spec(specification)
                .contentType(TestsConfigs.CONTENT_TYPE_XML)
                .header(TestsConfigs.HEADER_PARAM_ORIGIN, "http://localhost:8080")
                .pathParam("id", book.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        assertNotNull(content);

        assertTrue(content.contains("No records have been found for this id!"));
    }
    private void mockBook() throws ParseException {
        book.setId(1L);
        book.setTitle("Title test");
        book.setAuthor("Author test");
        String date = "2010-01-01";
        Date releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        book.setReleaseDate(releaseDate);
        book.setGenre("Test Genre");
    }
}
