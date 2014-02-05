package de.mq.mapping.util.proxy.support;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.core.convert.converter.Converter;

public class String2IntegerConverterTest {
	
	private static final Integer INT_VALUE = 19680528;
	private final Converter<String, Integer> converter =  new  String2IntegerConverter();
	
	@Test
	public final void convert() {
		Assert.assertEquals(INT_VALUE, converter.convert(String.valueOf(INT_VALUE)));
	}
	
	@Test
	public final void convertNull() {
		Assert.assertNull(converter.convert(null));
	}

}
