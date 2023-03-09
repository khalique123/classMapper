package com.example.jwt.annotation.mapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.example.jwt.annotation.MapToClass;
import com.example.jwt.annotation.MapToField;
import com.example.jwt.exception.MappingException;

import lombok.Getter;

@Getter
public class ClassMapper {
	private Class<?> mappedFromClass;
	private Class<?> mappedToClass;
	private Class<?>[] requiredAnnotationsOnMappedToClass;
	private Map<Field, Field> fieldMapping = new HashMap<>();

	public ClassMapper(Class<?> mappedFromClass) throws MappingException {
		try {
			this.mappedFromClass = mappedFromClass;

			//the from class should have annotation MapToClass
			if (!mappedFromClass.isAnnotationPresent(MapToClass.class)) {
				throw new MappingException(
						"Class " + mappedFromClass.getSimpleName() + " must be annotated with " + MapToClass.class);
			}

			MapToClass mapToClassAnnotaton = mappedFromClass.getAnnotation(MapToClass.class);

			this.mappedToClass = mapToClassAnnotaton.mappedTo();
			this.requiredAnnotationsOnMappedToClass = mapToClassAnnotaton.annotations();
			this.throwExceptionIfRequiredAnnotationsNotOnClass(this.mappedToClass,
					this.requiredAnnotationsOnMappedToClass);

			this.populateFieldMapping(this.mappedFromClass, this.mappedToClass);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new MappingException(e);
		}

	}

	public Map<Field, Field> getFieldMapping() {
		return this.fieldMapping;
	}

	@SuppressWarnings("unchecked")
	private void throwExceptionIfRequiredAnnotationsNotOnClass(Class<?> classObj,
			Class<?>[] requiredAnnotations) throws MappingException {

		for (Class<?> annotation : requiredAnnotations) {
			if (!classObj.isAnnotationPresent((Class<Annotation>)annotation)) {
				throw new MappingException(
						"Class " + classObj.getSimpleName() + " is not annotated with " + annotation.getSimpleName());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void populateFieldMapping(Class<?> mappedFromClass, Class<?> mappedToClass)
			throws NoSuchFieldException, SecurityException, MappingException {
		Field[] fromFields = mappedFromClass.getDeclaredFields();

		for (Field fromField : fromFields) {
			String toFieldName = "";

			if (fromField.isAnnotationPresent(MapToField.class)) {
				toFieldName = fromField.getAnnotation(MapToField.class).name();
			} else {
				toFieldName = fromField.getName();
			}

			Field mappedToField = mappedToClass.getDeclaredField(toFieldName);

			if (mappedToField == null) {
				throw new MappingException("Field mapping incorrect for field " + fromField.getName() + " in class "
						+ mappedFromClass.getSimpleName());
			} else {
				// check if required annotations are present
				for (Class<?> rquiredAnnotation : fromField.getAnnotation(MapToField.class).annotations()) {
					if (null == mappedToField.getAnnotation((Class<Annotation>)rquiredAnnotation)) {
						throw new MappingException(
								"Required annotation specified by field " + mappedFromClass.getSimpleName() + "."
										+ fromField.getName() + " not present on mapped field "
										+ mappedToClass.getSimpleName() + "." + mappedToField.getName());
					}
				}

				this.fieldMapping.put(fromField, mappedToField);
			}
		}
	}
}
