package com.example.demo.Service;

import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Model.Person;
import com.example.demo.Repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class PersonService {
    private Logger logger = Logger.getLogger(PersonService.class.getName());

    @Autowired
    PersonRepository personRepository;

    public Person findById(Long id){
        logger.info("Finding one person...");
        return personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records have been found for this id!"));
    }
    public List<Person> findAll(){
        return personRepository.findAll();
    }

    public Person create(Person person){
        logger.info("Creating a new person...");

        return personRepository.save(person);
    }
    public Person update(Person person){
        logger.info("Updating a new person...");
        Person entity = personRepository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records have been found for this id!"));
        entity.setFirstName(person.getLastName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        return personRepository.save(person);
    }

    public void delete(Long id){
        logger.info("Updating a new person...");
        Person entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records have been found for this id!"));
        personRepository.delete(entity);
        logger.info("Person with id: " + id + " was deleted");
    }
}
