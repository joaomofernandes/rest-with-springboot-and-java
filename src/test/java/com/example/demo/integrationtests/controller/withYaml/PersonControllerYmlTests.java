package com.example.demo.integrationtests.controller.withYaml;

import com.example.demo.configs.TestsConfigs;
import com.example.demo.integrationtests.Wrappers.WrapperPersonVO;
import com.example.demo.integrationtests.testcontainers.AbstractIntegrationTests;
import com.example.demo.integrationtests.vo.AccountCredentialsVO;
import com.example.demo.integrationtests.vo.PersonVO;
import com.example.demo.integrationtests.vo.TokenVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerYmlTests extends AbstractIntegrationTests {

    private static RequestSpecification specification;
    private static YMLMapper ymlMapper;

    private static PersonVO person;

    @BeforeAll
    public static void Setup(){
        ymlMapper = new YMLMapper();
        person = new PersonVO();
    }

    @Test
    @Order(0)
    public void authorization() throws JsonMappingException{
        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");
        var tokenVO = given()
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestsConfigs.CONTENT_TYPE_YAML, ContentType.TEXT)))
                .accept(TestsConfigs.CONTENT_TYPE_YAML)
                .contentType(TestsConfigs.CONTENT_TYPE_YAML)
                .basePath("/auth/signin")
                .port(TestsConfigs.SERVER_PORT)
                .body(user, ymlMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body().as(TokenVO.class, ymlMapper)
                .getAccessToken();

        specification = new RequestSpecBuilder().addHeader(TestsConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenVO)
                .setBasePath("/api/person/v1")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

    }

    @Test
    @Order(1)
    public void testCreate() throws JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestsConfigs.CONTENT_TYPE_YAML, ContentType.TEXT)))
                .accept(TestsConfigs.CONTENT_TYPE_YAML)
                .contentType(TestsConfigs.CONTENT_TYPE_YAML)
                        .header(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_ERUDIO)
                        .body(person, ymlMapper)
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body().as(PersonVO.class, ymlMapper);
        PersonVO createdPerson = content;
        person = createdPerson;

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());
        assertNotNull(createdPerson.getEnabled());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Folhinha", createdPerson.getFirstName());
        assertEquals("Pires", createdPerson.getLastName());
        assertEquals("Cuspe", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }
    @Test
    @Order(2)
    public void testUpdate() throws JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestsConfigs.CONTENT_TYPE_YAML, ContentType.TEXT)))
                .accept(TestsConfigs.CONTENT_TYPE_YAML)
                .contentType(TestsConfigs.CONTENT_TYPE_YAML)
                .header(TestsConfigs.HEADER_PARAM_ORIGIN, "http://localhost:8080")
                .body(person, ymlMapper)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body().as(PersonVO.class, ymlMapper);
        PersonVO persistedPerson = content;
        person = persistedPerson;

        assertNotNull(persistedPerson);
        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());
        assertNotNull(persistedPerson.getEnabled());

        assertTrue(persistedPerson.getId() > 0);

        assertEquals("Folhinha", persistedPerson.getFirstName());
        assertEquals("Pires", persistedPerson.getLastName());
        assertEquals("Cuspe", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
        assertTrue(persistedPerson.getEnabled());
    }
    @Test
    @Order(3)
    public void testCreateWithWrongOrigin() throws JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestsConfigs.CONTENT_TYPE_YAML, ContentType.TEXT)))
                .accept(TestsConfigs.CONTENT_TYPE_YAML)
                .contentType(TestsConfigs.CONTENT_TYPE_YAML)
                .header(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_SEMERU)
                .body(person, ymlMapper)
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
    @Order(4)
    public void findByID() throws JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestsConfigs.CONTENT_TYPE_YAML, ContentType.TEXT)))
                .accept(TestsConfigs.CONTENT_TYPE_YAML)
                .contentType(TestsConfigs.CONTENT_TYPE_YAML)
                .header(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_ERUDIO)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body().as(PersonVO.class, ymlMapper);
        PersonVO persistedPerson = content;
        person = persistedPerson;

        assertNotNull(persistedPerson);
        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());

        assertTrue(persistedPerson.getId() > 0);

        assertEquals("Folhinha", persistedPerson.getFirstName());
        assertEquals("Pires", persistedPerson.getLastName());
        assertEquals("Curral de Moinas", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
    }
    @Test
    @Order(5)
    public void findByIDWithWrongOrigin() throws JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestsConfigs.CONTENT_TYPE_YAML, ContentType.TEXT)))
                .accept(TestsConfigs.CONTENT_TYPE_YAML)
                .contentType(TestsConfigs.CONTENT_TYPE_YAML)
                .header(TestsConfigs.HEADER_PARAM_ORIGIN, TestsConfigs.ORIGIN_SEMERU)
                .pathParam("id", person.getId())
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
    @Order(6)
    public void findByIDNotFound() throws JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestsConfigs.CONTENT_TYPE_YAML, ContentType.TEXT)))
                .accept(TestsConfigs.CONTENT_TYPE_YAML)
                .contentType(TestsConfigs.CONTENT_TYPE_YAML)
                .header(TestsConfigs.HEADER_PARAM_ORIGIN, "http://localhost:8080")
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(404)
                .extract()
                .body().asString();

        assertNotNull(content);

        assertTrue(content.contains("No records have been found for this id!"));
    }
    @Test
    @Order(7)
    public void testDisableByID() throws JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestsConfigs.CONTENT_TYPE_XML)
                .header(TestsConfigs.HEADER_PARAM_ORIGIN, "http://localhost:8080")
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body().as(PersonVO.class, ymlMapper);
        PersonVO persistedPerson = content;
        person = persistedPerson;

        assertNotNull(persistedPerson);
        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());
        assertNotNull(persistedPerson.getEnabled());

        assertFalse(persistedPerson.getEnabled());

        assertEquals("Folhinha", persistedPerson.getFirstName());
        assertEquals("Pires", persistedPerson.getLastName());
        assertEquals("Curral de Moinas", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
    }
    @Test
    @Order(8)
    public void testDelete() throws JsonMappingException, JsonProcessingException {

        given().spec(specification)
                .contentType(TestsConfigs.CONTENT_TYPE_YAML)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(9)
    public void testFindAll() throws JsonMappingException, JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestsConfigs.CONTENT_TYPE_YAML)
                .accept(TestsConfigs.CONTENT_TYPE_YAML)
                .queryParams("page", 3, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(WrapperPersonVO.class, ymlMapper);

        WrapperPersonVO wrapper = content;
        var people = wrapper.getEmbeddedVO().getPersons();

        PersonVO foundPersonOne = people.get(0);

        assertNotNull(foundPersonOne.getId());
        assertNotNull(foundPersonOne.getFirstName());
        assertNotNull(foundPersonOne.getLastName());
        assertNotNull(foundPersonOne.getAddress());
        assertNotNull(foundPersonOne.getGender());

        assertTrue(foundPersonOne.getEnabled());

        assertEquals(Optional.of(677), foundPersonOne.getId());

        assertEquals("Alic", foundPersonOne.getFirstName());
        assertEquals("Terbrug", foundPersonOne.getLastName());
        assertEquals("3 Eagle Crest Court", foundPersonOne.getAddress());
        assertEquals("Male", foundPersonOne.getGender());

        PersonVO foundPersonSix = people.get(5);

        assertNotNull(foundPersonSix.getId());
        assertNotNull(foundPersonSix.getFirstName());
        assertNotNull(foundPersonSix.getLastName());
        assertNotNull(foundPersonSix.getAddress());
        assertNotNull(foundPersonSix.getGender());

        assertTrue(foundPersonSix.getEnabled());

        assertEquals(Optional.of(911), foundPersonSix.getId());

        assertEquals("Allegra", foundPersonSix.getFirstName());
        assertEquals("Dome", foundPersonSix.getLastName());
        assertEquals("57 Roxbury Pass", foundPersonSix.getAddress());
        assertEquals("Female", foundPersonSix.getGender());
    }

    @Test
    @Order(10)
    public void testFindByName() throws JsonMappingException, JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestsConfigs.CONTENT_TYPE_YAML)
                .accept(TestsConfigs.CONTENT_TYPE_YAML)
                .pathParam("firstName", "ayr")
                .queryParams("page", 0, "size", 6, "direction", "asc")
                .when()
                .get("findPersonByName/{firstName}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(WrapperPersonVO.class, ymlMapper);

        WrapperPersonVO wrapper = content;
        var people = wrapper.getEmbeddedVO().getPersons();

        PersonVO foundPersonOne = people.get(0);

        assertNotNull(foundPersonOne.getId());
        assertNotNull(foundPersonOne.getFirstName());
        assertNotNull(foundPersonOne.getLastName());
        assertNotNull(foundPersonOne.getAddress());
        assertNotNull(foundPersonOne.getGender());

        assertTrue(foundPersonOne.getEnabled());

        assertEquals(Optional.of(1), foundPersonOne.getId());

        assertEquals("Ayrton", foundPersonOne.getFirstName());
        assertEquals("Senna", foundPersonOne.getLastName());
        assertEquals("São Paulo", foundPersonOne.getAddress());
        assertEquals("Male", foundPersonOne.getGender());
    }

    @Test
    @Order(11)
    public void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {

        RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
                .setBasePath("/api/person/v1")
                .setPort(TestsConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        given().spec(specificationWithoutToken)
                .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                .when()
                .get()
                .then()
                .statusCode(403);
    }

    @Test
    @Order(12)
    public void testHATEOAS() throws JsonMappingException, JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestsConfigs.CONTENT_TYPE_JSON)
                .accept(TestsConfigs.CONTENT_TYPE_JSON)
                .queryParams("page", 3, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/677\"}}}"));
        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/846\"}}}"));
        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/714\"}}}"));

        assertTrue(content.contains("{\"first\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=0&size=10&sort=firstName,asc\"}"));
        assertTrue(content.contains("\"prev\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=2&size=10&sort=firstName,asc\"}"));
        assertTrue(content.contains("\"self\":{\"href\":\"http://localhost:8888/api/person/v1?page=3&size=10&direction=asc\"}"));
        assertTrue(content.contains("\"next\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=4&size=10&sort=firstName,asc\"}"));
        assertTrue(content.contains("\"last\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=100&size=10&sort=firstName,asc\"}}"));

        assertTrue(content.contains("\"page\":{\"size\":10,\"totalElements\":1007,\"totalPages\":101,\"number\":3}}"));
    }
    private void mockPerson() {
        person.setId(1L);
        person.setFirstName("Folhinha");
        person.setLastName("Pires");
        person.setAddress("Cuspe");
        person.setGender("Male");
        person.setEnabled(true);
    }
}
