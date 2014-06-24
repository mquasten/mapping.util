package de.mq.mapping.util.proxy.support;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.core.convert.converter.Converter;

public class String2DoubleConverterTest {
	
	private static final double AMOUNT = 47.11d;
	private Converter<String, Double> converter = new String2DoubleConverter();
	
	@Test
	public final void convert() {
		Assert.assertEquals(AMOUNT,converter.convert(String.valueOf(AMOUNT)));
	}
	
	@Test
	public final  void empty() {
		Assert.assertNull(converter.convert(String.valueOf(" ")));
		Assert.assertNull(converter.convert(null));
	}

}
