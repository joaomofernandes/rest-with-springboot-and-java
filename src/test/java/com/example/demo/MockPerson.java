package com.example.demo;

import com.example.demo.Model.Person;
import com.example.demo.data.vo.v1.PersonVO;

import java.util.ArrayList;
import java.util.List;

public class MockPerson {

    public Person mockEntity(Long number){
        Person person = new Person();
        person.setId(number.longValue());
        person.setFirstName("First Name test");
        person.setLastName("Last Name test");
        person.setAddress("Test address");
        person.setGender(((number % 2 == 0) ? "Male" : "Female"));

        return person;
    }

    public PersonVO mockVO(Long number){
        PersonVO person = new PersonVO();
        person.setId(number);
        person.setFirstName("First Name test");
        person.setLastName("Last Name test");
        person.setAddress("Test address");
        person.setGender(((number % 2 == 0) ? "Male" : "Female"));

        return person;
    }

    public List<Person> mockEntityList() {
        List<Person> persons = new ArrayList<Person>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockEntity(Long.valueOf(i)));
        }
        return persons;
    }
}
