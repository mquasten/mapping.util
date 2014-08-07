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
		Collection<Mapping> mappings = createMappings();
		

		
		final Class<MapBasedResponse> clazz = mapBasedClassFactory.createClass(mappings);
		
		
		final MapBasedResponse result = clazz.newInstance();
		Assert.assertTrue(result instanceof AbstractMapBasedResult);
		
		Assert.assertEquals(mappings,ReflectionTestUtils.getField(result, "mappings"));
	}
	@Test
	public final void createClassOnlyOncePermGenSpace() throws InstantiationException, IllegalAccessException {
		final Collection<Mapping> mappings = createMappings();
		final Class<MapBasedResponse> clazz = mapBasedClassFactory.createClass(mappings);
		for(int i = 1 ; i <= 1e6; i++) {
			 Assert.assertEquals(clazz, mapBasedClassFactory.createClass(mappings));
		}
		
		
	}
	
	

	private Collection<Mapping> createMappings() {
		final Mapping  parent = new Mapping("rows", null);
		final Collection<Mapping> childs = new ArrayList<>();
		childs.add(new Mapping( "value", "value"));
		childs.add(new Mapping("key", "key"));
		Collection<Mapping> mappings = new ArrayList<>();
		mappings.add(parent);
		return mappings;
	}
	
	@Test
	public final void builder() {
	
		Assert.assertTrue(  mapBasedClassFactory.mappingBuilder() instanceof MapBasedResultBuilderImpl);
	}
	

}
