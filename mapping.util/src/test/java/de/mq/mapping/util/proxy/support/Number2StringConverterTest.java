package de.mq.mapping.util.proxy.support;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.core.convert.converter.Converter;

public class Number2StringConverterTest {
	
	private  Converter<Number,String> converter = new Number2StringConverter();
	
	@Test
	public final void convertNumber() {
		Assert.assertEquals("8.15", converter.convert(new Double(8.15)));
	}
	
	@Test
	public final void convertNumberNull() {
		Assert.assertNull(converter.convert(null));
	}
	

}
