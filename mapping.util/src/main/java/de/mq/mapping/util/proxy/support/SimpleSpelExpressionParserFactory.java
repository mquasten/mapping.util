package de.mq.mapping.util.proxy.support;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class SimpleSpelExpressionParserFactory {
	
	@Bean()
	@Scope("prototype")
	public ELExpressionParser expexpressionParser() {
		return new SimpleSpelExpressionBuilderImpl();
	}

}
