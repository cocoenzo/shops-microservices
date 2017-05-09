package com.gft.demo;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.gft.demo.dao.PeopleDatabase;
import com.gft.demo.dao.Person;

@EnableAutoConfiguration
@EnableDiscoveryClient
@SpringBootApplication
public class MultiMsaPeopleApplication {

	@Autowired
	private PeopleDatabase db;
	
	public static void main(String[] args) {
		SpringApplication.run(MultiMsaPeopleApplication.class, args);
	}
	
	@PostConstruct
	public void includeElements() {
		Person person = Person.builder().id("1234").name("Javier").surname("Gonzalez").telephone(123456).email("javier.gonzalez@gft.com").build();
		db.savePerson(person);
		
		person = Person.builder().id("5678").name("Miguel").surname("Perez").telephone(123456).email("miguel.perez@gft.com").build();
		db.savePerson(person);
	}
}
