package com.example.demo.data.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;

import com.example.demo.annotation.Timed;
import com.example.demo.data.bean.Person;
import com.example.demo.data.repository.PersonRepository;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@ManagedResource(objectName = "Demo:type=JMX,name=PersonService")
public class PersonService {
	@Autowired 
	private PersonRepository personRepository;
	
	
	
	@Timed
	public List<Person> findAll() {
		return personRepository.findAll();
	}
	
	@Timed
	public List<Person> findByLastName(final String lastName)  {
		return personRepository.findByLastName(lastName);
	}
	
	@Timed
	public Person createNewPerson(final Person person) {
		return personRepository.save(person);
	}
	
	@Timed
	public void deletePerson(final Long id) {
		personRepository.delete(id);
	}
	
	@Transactional
	@Timed
	public Person updatePerson(final Person person) {
		checkIfPersonIdIsNull(person);
		
		Person oldPerson = personRepository.findOne(person.getId());
		
		checkIfPersonIsNull(oldPerson, person.getId());
		
		if(person.getFirstName() != null) {
			oldPerson.setFirstName(person.getFirstName());
		}
		if(person.getLastName() != null) { 
			oldPerson.setLastName(person.getLastName());
		}
		return personRepository.save(oldPerson);
	}
	
	@ManagedOperation
	public void logMessage(final String msg) {
		log.info("message from MBean: {}", msg);
	}
	
	private void checkIfPersonIdIsNull(final Person person) {
		if(person == null || person.getId() == null) {
			throw new BadRequestException("person or person id must not be null!");
		}
	}
	
	private void checkIfPersonIsNull(final Person person,final Long id) {
		if(person == null) {
			throw new NotFoundException(String.format("Person with id %s doesn't exist!", id));
		}
	}
}