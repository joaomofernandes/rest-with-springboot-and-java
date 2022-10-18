package com.example.demo;

import com.example.demo.Model.Person;
import com.example.demo.data.vo.v1.PersonVO;

public class MockPerson {

    public Person mockEntity(Integer number){
        Person person = new Person();
        person.setId(number.longValue());
        person.setFirstName("First Name test" + number);
        person.setLastName("Last name test" + number);
        person.setAddress("Test address" + number);
        person.setGender(((number % 2 == 0) ? "Male" : "Female"));

        return person;
    }

    public PersonVO mockVO(Integer number){
        PersonVO person = new PersonVO();
        person.setId(number.longValue());
        person.setFirstName("First Name test" + number);
        person.setLastName("Last name test" + number);
        person.setAddress("Test address" + number);
        person.setGender(((number % 2 == 0) ? "Male" : "Female"));

        return person;
    }
}
