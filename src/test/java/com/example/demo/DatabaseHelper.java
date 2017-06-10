package com.example.demo;

import com.example.demo.data.bean.Person;
import com.example.demo.data.service.PersonService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DatabaseHelper {
	private final PersonService personService;
	
	public void deleteAll() {
		personService.findAll().stream().map(person -> person.getId()).forEach(personService::deletePerson);
	}
	
	public void deletePersonByLastname(final String lastName) {		
		personService.findByLastName(lastName).stream()
			.map(person -> person.getId())
			.forEach(personService::deletePerson);
	}
	
	public Person createPerson(final Long id, final String firstName, final String lastName){
		final Person person = new Person();
		if(id != null) {
			person.setId(id);
		}
		person.setFirstName(firstName);
		person.setLastName(lastName);
		return person;
	}
}
