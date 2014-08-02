package de.mq.mapping.util.json.support;

import java.util.ArrayList;
import java.util.Collection;


import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.json.MapBasedResponseClassFactory;

public class MapBasedClassFactoryTest {
	
	private final MapBasedResponseClassFactory mapBasedClassFactory = new SimpleMapBasedResponseClassFactoryImpl();
	
	@Test
	public final void createClass() throws InstantiationException, IllegalAccessException {
		final Mapping  parent = new Mapping("rows", null);
		final Collection<Mapping> childs = new ArrayList<>();
		childs.add(new Mapping( "value", "value"));
		childs.add(new Mapping("key", "key"));
		Collection<Mapping> mappings = new ArrayList<>();
		mappings.add(parent);
		
		
		
		final Class<MapBasedResponse> clazz = mapBasedClassFactory.createClass(mappings);
		
		System.out.println(clazz);
		
		final MapBasedResponse result = clazz.newInstance();
		Assert.assertTrue(result instanceof AbstractMapBasedResult);
		
		Assert.assertEquals(mappings,ReflectionTestUtils.getField(result, "mappings"));
	}
	

}
