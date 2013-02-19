package de.mq.mapping.util.proxy.support;

import org.springframework.core.convert.converter.Converter;

public class Enum2StringConverter implements Converter<Enum<?>, String> {

	@Override
	public String convert(final Enum<?> source) {
		
		if( source == null){
			return "";
		}
		return source.name();
		
	}

}
