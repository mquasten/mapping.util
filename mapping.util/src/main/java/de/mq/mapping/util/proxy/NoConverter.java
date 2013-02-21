package de.mq.mapping.util.proxy;

import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
@Component
@Profile({"CGLib-Proxy", "Dynamic-Proxy"})
public class NoConverter implements Converter<Object,Object> {

	@Override
	public Object convert(final Object value) {
		return value;
	}

	

	
	
}
