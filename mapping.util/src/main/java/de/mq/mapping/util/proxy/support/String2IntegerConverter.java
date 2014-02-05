package de.mq.mapping.util.proxy.support;

import org.springframework.core.convert.converter.Converter;



public class String2IntegerConverter implements Converter<String, Integer> {
	
	
	@Override
	public final Integer convert(final String value) {
		if( value == null){
			return null;
		}
		return Integer.valueOf(value);
	}

}
