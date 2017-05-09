package com.gft.db.shops.service;

import java.util.Set;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gft.db.shops.data.Person;

@FeignClient("people")
public interface PeopleService {

	@RequestMapping(method = RequestMethod.GET, path ="/people/person/{id}")
	public Person readPerson(@PathVariable(name="id") final String id);
	
	@RequestMapping(method = RequestMethod.POST, path = "/people/")
	public Person save( final Person person);
	
	@RequestMapping(method = RequestMethod.GET, path ="/people/")
	public Set<Person> findAll();
}
