package de.mq.mapping.util.proxy.support;

import org.springframework.core.convert.converter.Converter;


public  abstract class AbstractString2EnumConverter  implements Converter<String, Enum<?>> {
	
	
	protected  abstract Enum<?> value(final String value);

	@Override
	public  final  Enum<?> convert(final String source) {
		
		if( source == null){
			return null;
		}
		if(source.trim().isEmpty()) {
			return null;
		} 
		return value(source);
	}

}
