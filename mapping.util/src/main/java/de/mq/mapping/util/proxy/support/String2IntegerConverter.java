package de.mq.mapping.util.proxy.support;

import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Profile({"CGLib-Proxy", "Dynamic-Proxy"})
public class String2IntegerConverter implements Converter<String, Integer> {
	
	
	@Override
	public final Integer convert(final String value) {
		if( value == null){
			return null;
		}
		
	
		return Integer.valueOf(value.toString());
	}

}
