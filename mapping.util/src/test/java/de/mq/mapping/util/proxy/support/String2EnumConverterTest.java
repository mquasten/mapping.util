package de.mq.mapping.util.proxy.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

public class String2EnumConverterTest {
	
	private static final String ENUM_STRING = "stringForEnum";

	@SuppressWarnings("unchecked")
	@Test
	public  void stringToEnum() {
		final AbstractString2EnumConverter  converter = Mockito.mock(AbstractString2EnumConverter.class);
		//Mockito.when(converter.convert(ENUM_STRING)).thenCallRealMethod();
		@SuppressWarnings("rawtypes")
		final Enum enumMock = Mockito.mock(Enum.class);
		Mockito.when(converter.value(ENUM_STRING)).thenReturn(enumMock);
		Assert.assertEquals(enumMock, converter.convert(ENUM_STRING));
	}
	
	@Test
	public final void stringToEnumNull() {
		final AbstractString2EnumConverter  converter = Mockito.mock(AbstractString2EnumConverter.class);
		Assert.assertNull(converter.convert(null));
		Mockito.verify(converter, Mockito.never()).value(Mockito.anyString());
	}
	
	@Test
	public final void stringToEnumEmpty() {
		final AbstractString2EnumConverter  converter = Mockito.mock(AbstractString2EnumConverter.class);
		Assert.assertNull(converter.convert(" "));
		Mockito.verify(converter, Mockito.never()).value(Mockito.anyString());
	}

	@Test
	public final void create() {
		new AbstractString2EnumConverter() {
			
			@Override
			protected Enum<?> value(String value) {
				
				return Mockito.mock(Enum.class);
			}
		};
	}
	
	

}
