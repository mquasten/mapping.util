package de.mq.mapping.util.json.support;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.json.support.MapBasedResponse.ChildField;
import de.mq.mapping.util.json.support.MapBasedResponse.InfoField;

public class MapBasedResultBuilderTest {
	
	private static final String FIELD = "field";
	private static final String KEY_PATH = "values";
	private static final String KEY = "rows";



	@Test
	public final void withParentmapping() {
		final MapBasedResultBuilderImpl builder = new MapBasedResultBuilderImpl();
		Assert.assertEquals(builder, builder.withParentMapping(KEY,  KEY_PATH));
		final  Mapping mapping = (Mapping) ReflectionTestUtils.getField(builder, "parent");
		Assert.assertEquals(Mapping.PARENT_FIELDNAME, mapping.key());
		Assert.assertEquals(KEY, ReflectionTestUtils.getField(mapping, "key"));
		
		@SuppressWarnings("unchecked")
		final Collection<String> paths = (Collection<String>) ReflectionTestUtils.getField(mapping, "paths");
		Assert.assertEquals(1, paths.size());
		Assert.assertEquals(KEY_PATH, paths.iterator().next());
		Assert.assertNull(ReflectionTestUtils.getField(mapping, FIELD));
		
		Assert.assertTrue(((Collection<?>)ReflectionTestUtils.getField(mapping, "childs")).isEmpty());
	
	}
	

	@Test
	public final void withFieldMapping() {
		final MapBasedResultBuilderImpl builder = new MapBasedResultBuilderImpl();
		Assert.assertEquals(builder, builder.withFieldMapping(KEY, InfoField.Status));
		@SuppressWarnings("unchecked")
		final Collection<Mapping> fieldMappings = (Collection<Mapping>) ReflectionTestUtils.getField(builder, "fieldMappings");
		Assert.assertEquals(1, fieldMappings.size());
		final Mapping result =  fieldMappings.iterator().next();
		Assert.assertEquals(InfoField.Status.field(), ReflectionTestUtils.getField(result, FIELD));
		Assert.assertEquals(KEY, ReflectionTestUtils.getField(result, "key"));
		Assert.assertEquals(InfoField.Status.field(), result.key());
		
		Assert.assertTrue(((Collection<?>)ReflectionTestUtils.getField(result, "childs")).isEmpty());
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public final void withChildMapping() {
		final MapBasedResultBuilderImpl builder = new MapBasedResultBuilderImpl();
		Assert.assertEquals(builder, builder.withChildMapping(ChildField.Value, KEY_PATH));
		
		final Collection<Mapping> childs = (Collection<Mapping>) ReflectionTestUtils.getField(builder, "childMappings");
		Assert.assertEquals(1, childs.size());
		final Mapping result = childs.iterator().next();
		Assert.assertNull(ReflectionTestUtils.getField(result, "key"));
		Assert.assertEquals("value", ReflectionTestUtils.getField(result, "field"));
		Assert.assertEquals(1, ((Collection<?>)ReflectionTestUtils.getField(result, "paths")).size());
		Assert.assertEquals(KEY_PATH, ((Collection<String>)ReflectionTestUtils.getField(result, "paths")).iterator().next());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public final void build() {
		final MapBasedResultBuilderImpl builder = new MapBasedResultBuilderImpl();
		final Mapping parent = Mockito.mock(Mapping.class);
		final Mapping child = Mockito.mock(Mapping.class);
		final Mapping field = Mockito.mock(Mapping.class);
		
		((Collection<Mapping>)ReflectionTestUtils.getField(builder, "childMappings")).add(child);
		((Collection<Mapping>)ReflectionTestUtils.getField(builder, "fieldMappings")).add(field);
		ReflectionTestUtils.setField(builder, "parent", parent);
		
		Collection<Mapping> results = (Collection<Mapping>) builder.build();
		Assert.assertEquals(2, results.size());
		Assert.assertTrue(results.contains(parent));
		Assert.assertTrue(results.contains(field));
		
		@SuppressWarnings("rawtypes")
		ArgumentCaptor<Collection> childsCaptor = ArgumentCaptor.forClass(Collection.class);
		Mockito.verify(parent).assignChilds(childsCaptor.capture());
		Assert.assertEquals(1, childsCaptor.getValue().size());
		Assert.assertEquals(child, childsCaptor.getValue().iterator().next());
		
	}

}
