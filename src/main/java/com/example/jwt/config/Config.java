package com.example.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.jwt.TestFromClass;
import com.example.jwt.annotation.mapper.ClassMapper;
import com.example.jwt.exception.MappingException;

@Configuration
public class Config {
	@Bean(name = "TestFromClass")
	public ClassMapper mapper() throws MappingException {
		return new ClassMapper(TestFromClass.class);
	}
}
