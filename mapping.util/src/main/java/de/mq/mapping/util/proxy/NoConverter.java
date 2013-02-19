package de.mq.mapping.util.proxy;

import org.springframework.core.convert.converter.Converter;

public class NoConverter implements Converter<Object,Object> {

	@Override
	public Object convert(final Object value) {
		return value;
	}

	

	
	
}
