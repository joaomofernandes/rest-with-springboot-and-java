package com.example.demo.Controllers;

import com.example.demo.Service.PersonService;
import com.example.demo.data.vo.v1.PersonVO;
import com.example.demo.data.vo.v2.PersonVOV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final AtomicLong counter = new AtomicLong();
    @Autowired
    private PersonService personService;

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE) // produces e consumes é necessário para a documentação do swagger
    public PersonVO findById(@PathVariable(name = "id") Long id) {
        return personService.findById(id);
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PersonVO> findAll(String id) {
        return personService.findAll();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PersonVO create(@RequestBody PersonVO person) {

        return personService.create(person);
    }
    @PostMapping(value = "/v2", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PersonVOV2 createV2(@RequestBody PersonVOV2 person) {

        return personService.createV2(person);
    }
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PersonVO update(@RequestBody PersonVO person) {
        return personService.update(person);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> Delete(@PathVariable(name = "id") Long id) {
        PersonVO person = personService.findById(id);
        personService.delete(id);
        String message = person.getFirstName() + " " + person.getLastName() + " was succesfully removed!";
        return new ResponseEntity<>(message, HttpStatus.NO_CONTENT);
    }
}

