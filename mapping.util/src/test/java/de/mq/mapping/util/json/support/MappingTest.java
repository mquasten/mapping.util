package de.mq.mapping.util.json.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.json.support.MapBasedResponse;
import de.mq.mapping.util.json.support.MapBasedResultRow;
import de.mq.mapping.util.json.support.Mapping;



public class MappingTest {
	
	private static final String PATH_VALUE_NEXT = "path2";
	private static final String PATH_VALUE_FIRST = "path1";
	private static final String PATHS_FIELD_NAME = "paths";
	private static final String FIELD_FIELD_NAME = "field";
	private static final String KEY_FIELD_NAME = "key";
	private static final String FIELD_VALUE = "field-value";
	private static final String KEY_VALUE = "key-value";

	@Test
	public final void constructor() {
		final Mapping<MapBasedResultRow> mapping =  new Mapping<>(KEY_VALUE , FIELD_VALUE , PATH_VALUE_FIRST , PATH_VALUE_NEXT);
		
		Assert.assertEquals(KEY_VALUE, ReflectionTestUtils.getField(mapping, KEY_FIELD_NAME));
		Assert.assertEquals(FIELD_VALUE, ReflectionTestUtils.getField(mapping,FIELD_FIELD_NAME));
		final List<String> paths = pathsField(mapping);
		Assert.assertEquals(2, paths.size());
		Assert.assertEquals(PATH_VALUE_FIRST, paths.get(0));
		Assert.assertEquals(PATH_VALUE_NEXT, paths.get(1));
	}

	@SuppressWarnings("unchecked")
	private List<String> pathsField(final Mapping<MapBasedResultRow> mapping) {
		return (List<String>) ReflectionTestUtils.getField(mapping,PATHS_FIELD_NAME);
	}
	
	@Test
	public final void constructorChild() {
		final Mapping<MapBasedResultRow> parent = new Mapping<>(KEY_VALUE, null);
		final Mapping<MapBasedResultRow> mapping =  new Mapping<>(parent,  FIELD_VALUE , PATH_VALUE_FIRST , PATH_VALUE_NEXT);
		Assert.assertNull(ReflectionTestUtils.getField(mapping, KEY_FIELD_NAME));
		Assert.assertEquals(FIELD_VALUE, ReflectionTestUtils.getField(mapping,FIELD_FIELD_NAME));
		final List<String> paths = pathsField(mapping);
		Assert.assertEquals(2, paths.size());
		Assert.assertEquals(PATH_VALUE_FIRST, paths.get(0));
		Assert.assertEquals(PATH_VALUE_NEXT, paths.get(1));
		
		 final Collection<Mapping<MapBasedResultRow>>  parentChilds = childs(parent);
		 Assert.assertEquals(1, parentChilds.size());
		 Assert.assertEquals(mapping, parentChilds.iterator().next());
	}

	@SuppressWarnings("unchecked")
	private Collection<Mapping<MapBasedResultRow>>  childs(final Mapping<MapBasedResultRow> parent) {
		return (Collection<Mapping<MapBasedResultRow>>) ReflectionTestUtils.getField(parent,"childs");
	}	
	
	@Test
	public final void field() {
		final Mapping<MapBasedResultRow> mapping =  new Mapping<>("hotScore" , "info" , "hotScore" );
		final MapBasedResponse  parent = new BasicMapBasedResult();
		final Map<String,Object> values = new HashMap<>();
		values.put("hotScore", 10);
		mapping.map(parent, null, "hotScore", values);
		Assert.assertEquals(10, ReflectionTestUtils.getField(parent, "info"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void fieldNotFound() {
		final Mapping<MapBasedResultRow> mapping =  new Mapping<>("hotScore" , "xxx" , "hotScore" );
	
		final Map<String,Object> values = new HashMap<>();
		values.put("hotScore", 10);
		mapping.map(new BasicMapBasedResult(), null, "hotScore", values);
	}        
	@Test
	public final void rows() {
		Mapping<MapBasedResultRow> parent = new Mapping<>("rows" , null);
		new Mapping<>(parent, "value");
		
		MapBasedResponse mapBasedResponse = new BasicMapBasedResult();
		final Collection<Map<String, Object>> rows = listResult();
		
		
		final Collection<MapBasedResultRow> results =  parent.map(mapBasedResponse, SimpleMapBasedResultRowImpl.class, "rows", rows);
		
		final Iterator<Map<String,Object>> it = rows.iterator();
		for(MapBasedResultRow result : results) {
			Assert.assertEquals(it.next(), result.composedValue());
		}
	
		
	}

	private Collection<Map<String, Object>> listResult() {
		final Collection<Map<String,Object>> rows = new ArrayList<>();
		final Map<String, Object> row1 = new HashMap<>();
		row1.put("name", "Nicole");
		row1.put("quality", "Platinium");
		row1.put("unit", "date");
		rows.add(row1);
		final Map<String, Object> row2 = new HashMap<>();
		row2.put("name", "Carmit");
		row2.put("quality", "Gold");
		row2.put("unit", "date");
		rows.add(row2);
		return rows;
	}
	@Test
	public final void rowsFromMap() {
		Mapping<MapBasedResultRow> parent = new Mapping<>("rows" , null);
		new Mapping<>(parent, "value");
		final Map<String, Object> row = new HashMap<>();
		row.put("name", "Nicole");
		row.put("quality", "Platinium");
		row.put("unit", "date");
		MapBasedResponse mapBasedResponse = new BasicMapBasedResult();
		final Collection<MapBasedResultRow> results =  parent.map( mapBasedResponse, SimpleMapBasedResultRowImpl.class, "rows", row);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(row, results.iterator().next().composedValue());
		
	
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void notMatchesForParent() {
		Mapping<MapBasedResultRow> parent = new Mapping<>( "row" , null);
		ReflectionTestUtils.setField(parent, "key", null);
	    Assert.assertTrue(parent.map( new BasicMapBasedResult(), SimpleMapBasedResultRowImpl.class, "rows", new HashMap<String,Object>()).isEmpty());
	}	
	
	@Test(expected=IllegalArgumentException.class)
	public final void notMatchesForRow() {
		Mapping<MapBasedResultRow> parent = new Mapping<>("rows" , null);
		Mapping<MapBasedResultRow> child = new Mapping<>(parent, "value");
		ReflectionTestUtils.setField(child, "key", "name");
		parent.map( new BasicMapBasedResult(), SimpleMapBasedResultRowImpl.class, "rows", listResult());
		
	}	
	
	@Test
	public final void rowNoMap() {
		Mapping<MapBasedResultRow> parent = new Mapping<>("rows" , null, "artist");
		
		new Mapping<>(parent, "value");
		final Map<String, Object> row = new HashMap<>();
		row.put("artist", "Kylie");
		final Collection<MapBasedResultRow> results =  parent.map( new BasicMapBasedResult(), SimpleMapBasedResultRowImpl.class, "rows", row);
		Assert.assertEquals(1, results.size());
		for(final MapBasedResultRow result : results){
			Assert.assertEquals("Kylie", result.singleValue(String.class));
			
		}
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void wrongPrperty() {
		Mapping<MapBasedResultRow> parent = new Mapping<>("rows" , null, "artist" , "name");
		new Mapping<>(parent, "value");
		final Map<String, Object> row = new HashMap<>();
		row.put("artist", "Kylie");
		parent.map( new BasicMapBasedResult(), SimpleMapBasedResultRowImpl.class, "rows", row);
	}
	
	@Test()
	public final void propertyNull() {
		Mapping<MapBasedResultRow> parent = new Mapping<>("rows" , null, "artist" , "name");
		new Mapping<>(parent, "value");
		parent.map( new BasicMapBasedResult(), SimpleMapBasedResultRowImpl.class, "rows", new HashMap<>());
		final Collection<MapBasedResultRow> results =  parent.map( new BasicMapBasedResult(), SimpleMapBasedResultRowImpl.class, "rows", new HashMap<>());
		Assert.assertEquals(1, results.size());
		for(final MapBasedResultRow result : results){
			Assert.assertNull(result.singleValue(String.class));
		}
		
	}
	
	@Test()
	public final void parentNotMatch() {
	Mapping<MapBasedResultRow> parent = new Mapping<>("rows" , null);
	new Mapping<>(parent, "value");
	
	MapBasedResponse mapBasedResponse = new BasicMapBasedResult();
	System.out.println(parent.map(mapBasedResponse, SimpleMapBasedResultRowImpl.class, "dontLetMeGetMe", listResult()));
	
	}
	
	class BasicMapBasedResult extends AbstractMapBasedResult {

		private static final long serialVersionUID = 1L;

		@Override
		protected void configure() {
			
			
		}
		
	}
}
