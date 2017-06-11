package com.example.demo.data.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.DatabaseHelper;
import com.example.demo.data.bean.Person;
import com.example.demo.data.service.PersonService;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:/application-test.properties")
public class PersonServiceTest {

	@Autowired
	private PersonService personService;
	
	private DatabaseHelper dbHelper;
	
	@Before
	public void setup() {
		dbHelper = new DatabaseHelper(personService);
		dbHelper.deleteAll();
		personService.createNewPerson(dbHelper.createPerson(null, "wangjun", "shi"));
		personService.createNewPerson(dbHelper.createPerson(null, "hong", "li"));
	}
	
	@Test
	public void testCreateAndFind() {
		
		assertThat(2, is(personService.findAll().size()));
		assertThat(1, is(personService.findByLastName("li").size()));
		assertThat(1, is(personService.findByLastName("shi").size()));

		assertThat("hong", is(personService.findByLastName("li").get(0).getFirstName()));
		assertThat("wangjun", is(personService.findByLastName("shi").get(0).getFirstName()));
	}
	
	@Test
	public void testDelete() {
		dbHelper.deletePersonByLastname("shi");
		
		assertThat(1, is(personService.findAll().size()));
		assertThat(1, is(personService.findByLastName("li").size()));
		assertThat(0, is(personService.findByLastName("shi").size()));
		
		dbHelper.deletePersonByLastname("li");
		assertThat(0, is(personService.findAll().size()));
	}
	
	@Test
	public void testUpdate() {
		final String lastName = "shi";
		final String oldFirstName = "wangjun";
		final String newFirstName = "yikui";
		
		Person person = personService.findByLastName(lastName).get(0);
		assertThat(oldFirstName, is(person.getFirstName()));
		
		person.setFirstName(newFirstName);
		personService.updatePerson(person);
		
		person = personService.findByLastName(lastName).get(0);
		assertThat(newFirstName, is(person.getFirstName()));
		
		
	}
	
	@Test(expected = BadRequestException.class)
	public void testUpdateWillThrowBadRequestExceptionIfTheGivenPersonIsNull(){
		personService.updatePerson(null);
	}
	
	@Test(expected = BadRequestException.class)
	public void testUpdateWillThrowBadRequestExceptionIfTheGivenPersonIdIsNull(){
		Person person = personService.findByLastName("shi").get(0);
		person.setId(null);
		personService.updatePerson(person);
	}
	
	@Test(expected = NotFoundException.class)
	public void testUpdateWillThrowNotFoundExceptionIfTheGivenPersonDoesNotExist(){
		Person person = personService.findByLastName("shi").get(0);
		person.setId(-1L);
		personService.updatePerson(person);
	}
}
