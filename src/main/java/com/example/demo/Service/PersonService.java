package com.example.demo.Service;

import com.example.demo.Controllers.v1.PersonControllerV1;
import com.example.demo.Exceptions.RequiredObjectIsNullException;
import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Model.Person;
import com.example.demo.Repository.PersonRepository;
import com.example.demo.data.vo.v1.PersonVO;
import com.example.demo.data.vo.v2.PersonVOV2;
import org.modelmapper.Converter;
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
import org.springframework.hateoas.UriTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.function.Function;
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
    @Autowired
    PagedResourcesAssembler<PersonVO> assembler;

    public PersonVO findById(Long id){
        logger.info("Finding one person...");
        Person person = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records have been found for this id!"));
        PersonVO vo = new PersonVO(person.getId(), person.getFirstName(), person.getLastName(), person.getAddress(), person.getGender());
        vo.add(linkTo(methodOn(PersonControllerV1.class).findById(id)).withSelfRel());
        return vo;
    }
    public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable){

        Page personPage = personRepository.findAll(pageable);
        var personVOsPage = personPage.map((Function<Person, PersonVO>) p -> new ModelMapper().map(p, PersonVO.class)
                .add(linkTo(methodOn(PersonControllerV1.class).findById(p.getId())).withSelfRel()));

        Link link = linkTo(methodOn(PersonControllerV1.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(personVOsPage, link);
    }

    public PagedModel<EntityModel<PersonVO>> findPersonByName(Pageable pageable, String firstName){

        Page personPage = personRepository.findPersonByName(firstName, pageable);
        var personVOsPage = personPage.map((Function<Person, PersonVO>) p -> new ModelMapper().map(p, PersonVO.class)
                .add(linkTo(methodOn(PersonControllerV1.class).findById(p.getId())).withSelfRel()));

        Link link = linkTo(methodOn(PersonControllerV1.class)
                .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(personVOsPage, link);
    }
    public PersonVO create(PersonVO person){
        if(person ==  null){
            throw new RequiredObjectIsNullException("It is not allowed to persist a null object!");
        }
        logger.info("Creating a new person...");
        Person personToSave = mapper.map(person, Person.class);
        personRepository.save(personToSave);
        person.add(linkTo(methodOn(PersonControllerV1.class).findById(person.getId())).withSelfRel());
        return person;
    }
    public PersonVO update(PersonVO person){
        if(person ==  null){
            throw new RequiredObjectIsNullException("It is not allowed to persist a null object!");
        }
        logger.info("Updating a new person...");
        Person personToEdit = mapper.map(person, Person.class);
        personRepository.save(personToEdit);
        person.add(linkTo(methodOn(PersonControllerV1.class).findById(person.getId())).withSelfRel());
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
    @Transactional
    public PersonVO disablePerson(Long id){
        logger.info("Disabling one person...");
        personRepository.disablePerson(id);
        Person person = personRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records have been found for this id!"));
        PersonVO vo = new PersonVO(person.getId(), person.getFirstName(), person.getLastName(), person.getAddress(), person.getGender());
        vo.add(linkTo(methodOn(PersonControllerV1.class).findById(id)).withSelfRel());
        return vo;
    }
}
