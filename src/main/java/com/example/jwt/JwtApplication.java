package com.example.jwt;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.convert.PeriodFormat;

import com.example.jwt.annotation.mapper.ClassMapper;

@SpringBootApplication
public class JwtApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(JwtApplication.class, args);
	}

	@Autowired
	@Qualifier("TestFromClass")
	private ClassMapper classMapper;

	@Override
	public void run(String... args) throws Exception {
		TestFromClass fromClass = new TestFromClass(true, true);
		StringBuilder values = new StringBuilder();
		int count = 0;

		for(Field field : fromClass.getClass().getDeclaredFields()) {
			if(field.getType().isAssignableFrom(boolean.class)) {
				Boolean value = (Boolean) returnGetterMethod(field).invoke(fromClass);

				if(value.booleanValue()) {
					for (Map.Entry<Field, Field> entry : classMapper.getFieldMapping().entrySet()) {
						Field fromField = entry.getKey();
						Field toField = entry.getValue();

						if(field.getName().equals(fromField.getName()) && toField.isAnnotationPresent(PeriodFormat.class)) {

							if(count == 0) {
								values.append(toField.getAnnotation(PeriodFormat.class).value().toString());
							} else {
								values.append(", " + toField.getAnnotation(PeriodFormat.class).value().toString());
							}
							count++;
						}
					}
				}
			}
		}

		System.out.println(values);
	}

	private Method returnGetterMethod(Field field) throws IntrospectionException {
		return new PropertyDescriptor(field.getName(), TestFromClass.class).getReadMethod();
	}
}
