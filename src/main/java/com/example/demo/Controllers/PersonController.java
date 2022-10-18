package com.example.demo.Controllers;

import com.example.demo.Model.Person;
import com.example.demo.Service.PersonService;
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
    public Person findById(@PathVariable(name = "id") Long id) {

        return personService.findById(id);
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Person> findAll(String id) {

        return personService.findAll();
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Person create(@RequestBody Person person) {

        return personService.create(person);
    }
    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Person update(@RequestBody Person person) {

        return personService.update(person);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> Delete(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(personService.delete(id), HttpStatus.NO_CONTENT);
    }
}

