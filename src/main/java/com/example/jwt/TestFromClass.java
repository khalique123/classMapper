package com.example.jwt;

import org.springframework.boot.convert.PeriodFormat;
import org.springframework.stereotype.Component;

import com.example.jwt.annotation.MapToClass;
import com.example.jwt.annotation.MapToField;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@MapToClass(mappedTo = TestToClass.class, annotations = {Component.class})
@AllArgsConstructor
@Getter
@Setter
public class TestFromClass {
	@MapToField(name = "col1", annotations = {PeriodFormat.class})
	private boolean pol1;

	@MapToField(name = "col2", annotations = {PeriodFormat.class})
	private boolean pol2;
}
