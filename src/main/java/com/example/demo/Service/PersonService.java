package com.example.demo.Service;

import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Model.Person;
import com.example.demo.Repository.PersonRepository;
import com.example.demo.data.vo.v1.PersonVO;
import com.example.demo.data.vo.v2.PersonVOV2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class PersonService {
    private Logger logger = Logger.getLogger(PersonService.class.getName());

    private ModelMapper mapper = new ModelMapper();

    @Autowired
    PersonRepository personRepository;
    @Autowired
    PersonMapper personMapper;

    public PersonVO findById(Long id){
        logger.info("Finding one person...");
        Person person = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records have been found for this id!"));
        return new PersonVO(person.getId(), person.getFirstName(), person.getLastName(), person.getAddress(), person.getGender());
    }
    public List<PersonVO> findAll(){

        return personRepository.findAll().stream().map(person ->
                        new PersonVO(person.getId(), person.getFirstName(), person.getLastName(), person.getAddress(), person.getGender()))
                .collect(Collectors.toList());
    }

    public PersonVO create(PersonVO person){
        logger.info("Creating a new person...");
        Person personToSave = mapper.map(person, Person.class);
        personRepository.save(personToSave);
        return person;
    }
    public PersonVO update(PersonVO person){
        logger.info("Updating a new person...");
        Person personToEdit = mapper.map(person, Person.class);
        personRepository.save(personToEdit);
        return person;
    }

    public void delete(Long id){
        logger.info("Updating a new person...");
        Person entity = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records have been found for this id!"));
        personRepository.delete(entity);
        logger.info("Person with id: " + id + " was deleted");
    }

    public PersonVOV2 createV2(PersonVOV2 person) {
        logger.info("Creating a new person...");
        Person personToSave = personMapper.convertVOToEntity(person);
        PersonVOV2 vov2 = personMapper.convertEntityToVO(personToSave);
        person = personMapper.convertEntityToVO(personRepository.save(personToSave));

        return person;
    }
}
