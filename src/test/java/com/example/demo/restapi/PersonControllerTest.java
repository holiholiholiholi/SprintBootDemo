package com.example.demo.restapi;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.demo.DatabaseHelper;
import com.example.demo.data.bean.Person;
import com.example.demo.data.service.PersonService;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
@TestPropertySource("classpath:/application-test.yml")
public class PersonControllerTest {
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private PersonService personService;

	private MockMvc mvc;
	
	private DatabaseHelper dbHelper;

	@Before
	public void setup() {
		dbHelper = new DatabaseHelper(personService);
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.build();
		dbHelper.deleteAll();
	}
	
	@Test
	public void testFindAll() throws Exception {
		personService.createNewPerson(dbHelper.createPerson(null, "wangjun", "shi"));
		personService.createNewPerson(dbHelper.createPerson(null, "hong", "li"));

		
		mvc.perform(get("/person/all"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0]['firstName']").value("wangjun"))
			.andExpect(jsonPath("$[0]['lastName']").value("shi"))
			.andExpect(jsonPath("$[1]['firstName']").value("hong"))
			.andExpect(jsonPath("$[1]['lastName']").value("li"));
	}
	
	@Test
	public void testFindPersonByLastName() throws Exception {
		personService.createNewPerson(dbHelper.createPerson(null, "wangjun", "shi"));
		personService.createNewPerson(dbHelper.createPerson(null, "hong", "li"));
		personService.createNewPerson(dbHelper.createPerson(null, "ying", "li"));

		
		mvc.perform(get("/person/lastname/li"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(2)))
			.andExpect(jsonPath("$[0]['firstName']").value("hong"))
			.andExpect(jsonPath("$[0]['lastName']").value("li"))
			.andExpect(jsonPath("$[1]['firstName']").value("ying"))
			.andExpect(jsonPath("$[1]['lastName']").value("li"));
	}
	
	@Test
	public void testCreateNewPerson() throws Exception {
		mvc.perform(post("/person/new")
					.contentType(MediaType.APPLICATION_JSON)
					.content("{\"firstName\":\"yikui\", \"lastName\":\"shi\"}"))
			.andExpect(status().isCreated());
		
		List<Person> personList  = personService.findAll();
		assertThat(1, is(personList.size()));
		
		Person person = personList.get(0);
		assertThat("yikui", is(person.getFirstName()));
		assertThat("shi", is(person.getLastName()));
	}
	
	@Test
	public void testDeletePerson() throws Exception {
		Person person = personService.createNewPerson(dbHelper.createPerson(null, "wangjun", "shi"));
		personService.createNewPerson(dbHelper.createPerson(null, "hong", "li"));

		Long id = person.getId();
		
		mvc.perform(delete("/person/id/"+id))
			.andExpect(status().isOk());
		
		List<Person> personList = personService.findAll();
		assertThat(1, is(personList.size()));
		person = personList.get(0);
		assertThat("hong", is(person.getFirstName()));
		assertThat("li", is(person.getLastName()));
	}
	
	@Test
	public void testUpdatePerson() throws Exception {
		Person person = personService.createNewPerson(dbHelper.createPerson(null, "wangjun", "shi"));
		personService.createNewPerson(dbHelper.createPerson(null, "hong", "li"));

		mvc.perform(patch("/person/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\":"+person.getId()+",\"firstName\":\"yikui\", \"lastName\":\"shi\"}"))
		.andExpect(status().isOk());
		
		List<Person> personList = personService.findByLastName("shi");
		assertThat(1, is(personList.size()));
		person =personList.get(0);
		assertThat("yikui", is(person.getFirstName()));
		assertThat("shi", is(person.getLastName()));
	}
	
	@Test
	public void testUpdatePerson400ShouldBeReturnedIfPersonIdIsMissing() throws Exception {
		Person person = personService.createNewPerson(dbHelper.createPerson(null, "wangjun", "shi"));
		personService.createNewPerson(dbHelper.createPerson(null, "hong", "li"));
		
		mvc.perform(patch("/person/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"firstName\":\"yikui\", \"lastName\":\"shi\"}"))
		.andExpect(status().isBadRequest());
		
		List<Person> personList = personService.findByLastName("shi");
		assertThat(1, is(personList.size()));
		person =personList.get(0);
		assertThat("wangjun", is(person.getFirstName()));
		assertThat("shi", is(person.getLastName()));
	}
	
	@Test
	public void testUpdatePerson404ShouldBeReturnedIfPersonIdDoesNotExist() throws Exception {
		Person person = personService.createNewPerson(dbHelper.createPerson(null, "wangjun", "shi"));
		personService.createNewPerson(dbHelper.createPerson(null, "hong", "li"));
		
		Long invalidPersonId = -1L;
		
		mvc.perform(patch("/person/update")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"id\":"+invalidPersonId+", \"firstName\":\"yikui\", \"lastName\":\"shi\"}"))
		.andExpect(status().isNotFound());
		
		List<Person> personList = personService.findByLastName("shi");
		assertThat(1, is(personList.size()));
		person =personList.get(0);
		assertThat("wangjun", is(person.getFirstName()));
		assertThat("shi", is(person.getLastName()));
	}
}
