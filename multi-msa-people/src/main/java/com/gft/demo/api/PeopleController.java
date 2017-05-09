package com.gft.demo.api;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gft.demo.dao.PeopleDatabase;
import com.gft.demo.dao.Person;

@ExposesResourceFor(Person.class)
@RestController
@RequestMapping(value = "/people", produces = "application/json")
public class PeopleController {

	@Autowired
	private PeopleDatabase dao;
	
	@RequestMapping(method= RequestMethod.GET, path="/")
	public @ResponseBody Set<Person> findAll(){
		return dao.findAll();
	}
	
	@RequestMapping(method= RequestMethod.GET, path="/person/{id}")
	public Person read(@PathVariable final String id){
		return dao.readPerson(id).get();
	}
	
	@RequestMapping(method = RequestMethod.POST, path = "/")
	public Person save(@RequestBody final Person person){
		return dao.savePerson(person).get();
	}
}
