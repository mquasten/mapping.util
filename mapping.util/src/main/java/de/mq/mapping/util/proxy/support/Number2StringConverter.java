package de.mq.mapping.util.proxy.support;

import org.springframework.context.annotation.Profile;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@Profile({"CGLib-Proxy", "Dynamic-Proxy"})
public class Number2StringConverter implements Converter<Number,String>{

	@Override
	public String convert(final Number value) {
		return String.valueOf(value);
	}

	

	

}
