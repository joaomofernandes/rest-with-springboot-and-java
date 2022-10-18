package com.example.demo;

import com.example.demo.data.vo.v1.PersonVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class UnitTests {

	MockPerson inputObject = new MockPerson();
	private ModelMapper mapper = new ModelMapper();


	@Test
	void parseEntitytoVOTest() {
		Random rand = new Random();
		int number = rand.nextInt(10);
		PersonVO output = mapper.map(inputObject.mockEntity(number), PersonVO.class);
		assertEquals("First Name test"+ number, output.getFirstName());
		assertEquals("Last name test"+ number, output.getLastName());
		assertEquals("Test address" + number, output.getAddress());
		assertEquals("Test address" + number, output.getAddress());
		assertEquals(((number % 2 == 0) ? "Male" : "Female"), output.getGender());
	}

}
