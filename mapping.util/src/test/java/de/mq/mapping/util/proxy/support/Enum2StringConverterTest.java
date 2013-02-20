package de.mq.mapping.util.proxy.support;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.core.convert.converter.Converter;

public class Enum2StringConverterTest {
	
	@Test
	public final void testConveter() {
		
		
		final Converter<Enum<?>, String> converter = new Enum2StringConverter();
		Assert.assertEquals(EnumMock.Constant.name(), converter.convert(EnumMock.Constant));
		
	}
	
	
	

}

enum EnumMock {
	Constant;
}