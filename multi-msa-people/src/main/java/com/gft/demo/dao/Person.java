package com.gft.demo.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Wither;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Wither
public class Person {

	@NonNull
	private String id;
	@NonNull
	private String name;
	@NonNull
	private String surname;
	private int telephone;
	private String email;
	
}
