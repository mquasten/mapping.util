package de.mq.mapping.util.proxy.model;

import org.springframework.core.convert.converter.Converter;



public class String2IntegerConverter implements Converter<String, Integer> {
	
	
	@Override
	public final Integer convert(final String value) {
		return Integer.valueOf(value);
	}

}
