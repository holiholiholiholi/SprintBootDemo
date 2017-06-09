package com.example.demo.data.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.data.bean.Person;


public interface PersonRepository extends CrudRepository<Person, Long> {
	List<Person> findAll();
	
	List<Person> findByLastName(String lastName);
	
	@SuppressWarnings("unchecked")
	
	Person save(Person person);
	
	void delete(Long id);
}
