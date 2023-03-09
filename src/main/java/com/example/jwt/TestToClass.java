package com.example.jwt;

import org.springframework.boot.convert.PeriodFormat;
import org.springframework.boot.convert.PeriodStyle;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class TestToClass {
	
	@PeriodFormat(PeriodStyle.SIMPLE)
	private int col1;
	
	@PeriodFormat(PeriodStyle.ISO8601)
	private int col2;
}
