package de.mq.mapping.util.proxy.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

public class String2DoubleConverter implements Converter<String, Double>  {

	@Override
	public Double convert(final String source) {
		if( ! StringUtils.hasText(source)){
			return null;
		}
		
		return Double.valueOf(source);
	}

}
