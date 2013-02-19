package de.mq.mapping.util.proxy.support;

import org.springframework.core.convert.converter.Converter;

public class Number2StringConverter implements Converter<Number,String>{

	@Override
	public String convert(final Number value) {
		return String.valueOf(value);
	}

	

	

}
