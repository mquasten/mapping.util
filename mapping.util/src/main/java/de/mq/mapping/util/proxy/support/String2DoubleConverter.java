package de.mq.mapping.util.proxy.support;

import org.springframework.core.convert.converter.Converter;

public class String2DoubleConverter implements Converter<String, Double>  {

	@Override
	public Double convert(final String source) {
		if( source == null){
			return null;
		}
		
		return Double.valueOf(source);
	}

}
