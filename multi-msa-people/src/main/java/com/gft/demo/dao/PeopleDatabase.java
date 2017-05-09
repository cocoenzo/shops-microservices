package com.gft.demo.dao;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;
@Repository
public class PeopleDatabase {

	private final Map<String, Person> people = new ConcurrentHashMap<>();

	public Optional<Person> savePerson(Person person) {
		return Optional.ofNullable(this.people.put(person.getId(), person));
	}
	
	public Optional<Person> readPerson(String id){
		return Optional.ofNullable(people.get(id));
	}
	
	public Optional<Person> readPerson(String name, String surname){
		
		Person person = null;
		people.forEach((k,v)-> {
			if(name.equalsIgnoreCase(v.getName()) && surname.equalsIgnoreCase(v.getSurname())){
				person.builder().id(v.getId()).name(v.getName()).surname(v.getSurname()).email(v.getEmail()).telephone(v.getTelephone()).build();
			}
		});
		return Optional.ofNullable(person);
	}
	public Set<Person> findAll() {
		return new HashSet<Person>(people.values());
	}

}
