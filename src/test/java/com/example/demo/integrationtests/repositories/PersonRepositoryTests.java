package com.example.demo.integrationtests.repositories;

import com.example.demo.Model.Person;
import com.example.demo.Repository.PersonRepository;
import com.example.demo.configs.TestsConfigs;
import com.example.demo.integrationtests.Wrappers.WrapperPersonVO;
import com.example.demo.integrationtests.testcontainers.AbstractIntegrationTests;
import com.example.demo.integrationtests.vo.PersonVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.cassandra.AutoConfigureDataCassandra;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonRepositoryTests extends AbstractIntegrationTests {

    @Autowired
    public PersonRepository repository;

    private static Person person;

    @BeforeAll
    public static void setup(){
        person = new Person();
    }

    @Test
    @Order(0)
    public void testFindByName() throws JsonMappingException, JsonProcessingException {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "firstName"));

        person = repository.findPersonByName("ani", pageable).getContent().get(0);


        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());

        assertTrue(person.getEnabled());

        assertEquals(Optional.of(1), person.getId());

        assertEquals("Aniceto", person.getFirstName());
        assertEquals("Rui", person.getLastName());
        assertEquals("Rua dos Penedos", person.getAddress());
        assertEquals("Male", person.getGender());
    }
    @Test
    @Order(0)
    public void testDisablePerson() throws JsonMappingException, JsonProcessingException {
        repository.disablePerson(person.getId());

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "firstName"));

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());

        assertFalse(person.getEnabled());

        assertEquals(Optional.of(1), person.getId());

        assertEquals("Aniceto", person.getFirstName());
        assertEquals("Rui", person.getLastName());
        assertEquals("Rua dos Penedos", person.getAddress());
        assertEquals("Male", person.getGender());
    }
}
