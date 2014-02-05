package de.mq.mapping.util.proxy.support;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.mapping.util.proxy.model.Artist;
import de.mq.mapping.util.proxy.model.ArtistAO;
import de.mq.mapping.util.proxy.model.ArtistImpl;

public class JSFStringValueTest {
	
	private BeanResolver beanResolver;
	private static final String KEY_VALUE = "hotScore";
	private static final String ERROR_VALUE = "don't let me get me";
	
	final Converter<String, Integer> string2IntegerConverter = new  String2IntegerConverter();
	final Converter<Number, String> number2StringConverter = new Number2StringConverter();
	
	@Before()
	public final void setup() {
		beanResolver = Mockito.mock(BeanResolver.class);
		Mockito.when(beanResolver.getBeanOfType(String2IntegerConverter.class)).thenReturn((String2IntegerConverter) string2IntegerConverter);
		Mockito.when(beanResolver.getBeanOfType(Number2StringConverter.class)).thenReturn((Number2StringConverter) number2StringConverter);
	}

	@Test
	public final void testStringSetterWithConverter() {
		final AOProxyFactory factory = new BeanConventionCGLIBProxyFactory();
		
		final Artist domain = new ArtistImpl("Kylie");
		final ModelRepository  modelRepository = new ModelRepositoryImpl(beanResolver, domain) ; 
		final ArtistAO proxy = factory.createProxy(ArtistAO.class,modelRepository);
		
		
		proxy.setHotScoreAsString("10");
		
		
		Assert.assertFalse(modelRepository.hasError(ArtistImpl.class, KEY_VALUE));
		
		
		Assert.assertEquals(10, domain.hotScore().intValue());
		
	
		
	}
	
	
	@Test
	public final void testStringSetterWithWrongValue() {
		final AOProxyFactory factory = new BeanConventionCGLIBProxyFactory();
		
		final Artist domain = new ArtistImpl("T1");
		final ModelRepository  modelRepository = new ModelRepositoryImpl(beanResolver, domain) ; 
		final ArtistAO proxy = factory.createProxy(ArtistAO.class, modelRepository);
		
		
		proxy.setHotScoreAsString(ERROR_VALUE);
		
		
		@SuppressWarnings("unchecked")
		final Map<Key,Object> errors = (Map<Key, Object>) ReflectionTestUtils.getField(modelRepository, "modelItems");
	
		
		Assert.assertTrue(modelRepository.hasError(ArtistImpl.class, KEY_VALUE));
		Assert.assertTrue(errors.containsKey(new KeyImpl(ArtistImpl.class, KEY_VALUE)));
		Assert.assertEquals(ERROR_VALUE, errors.get(new KeyImpl(ArtistImpl.class, KEY_VALUE)));
		Assert.assertNull(domain.hotScore());
		
		
		
	}
	
	
	
	@Test
	public final void testStringGetterWithConverter() {
		final AOProxyFactory factory = new BeanConventionCGLIBProxyFactory();
	
		final Artist domain = new ArtistImpl("Kylie", 10);
		final ModelRepository  modelRepository = new ModelRepositoryImpl(beanResolver, domain) ; 
		final ArtistAO proxy = factory.createProxy(ArtistAO.class, modelRepository);
		
		Assert.assertEquals(String.valueOf(domain.hotScore()), proxy.gethotScoreAsString());
	}
	
	
	@Test
	public final void testStringGetterValueFromMap() {
		final AOProxyFactory factory = new BeanConventionCGLIBProxyFactory();
		

		final Artist domain = new ArtistImpl("Kylie");
		final ModelRepository modelRepository = new ModelRepositoryImpl(beanResolver, domain);
		final ArtistAO proxy = factory.createProxy(ArtistAO.class, modelRepository);
		@SuppressWarnings("unchecked")
		final Map<Key,Object> errors = (Map<Key, Object>) ReflectionTestUtils.getField(modelRepository, "modelItems");
		errors.put(new KeyImpl(ArtistImpl.class, KEY_VALUE), ERROR_VALUE);
		
		
		Assert.assertEquals(ERROR_VALUE, proxy.gethotScoreAsString());
	}
	
	
	

}
