package com.far.ionicapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.far.ionicapp.services.DBService;

@Configuration
public class TestConfig {
	
	@Autowired
	private DBService dbService;
	
	@Bean
	public boolean instantiateDataBase() {
		dbService.instatiateTestDatabase();
		return true;
	}

}
