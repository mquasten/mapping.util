package de.mq.mapping.util.json.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;


import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.json.support.MapBasedResponse.InfoField;





public class MapBasedResponseTest {
	
	private static final String STATUS_OK = "OK";
	private static final String QUALITY = "platinium";
	private static final String UNIT = "private date";
	private static final String SINGLE_VALUE = "19680528";

	
	
	@SuppressWarnings("unchecked")
	@Test
	public final void testMapping() {
		
		final MapBasedResponse mapBasedResponse = new BasicMapBasedResult();
		
		final Collection<Mapping> mappings =  mappingsField(mapBasedResponse);
	    Assert.assertEquals(1, mappings.size());
	    Assert.assertEquals("rows", ReflectionTestUtils.getField(mappings.iterator().next(), "key"));
	    Assert.assertEquals(2,  ((Collection<Mapping>) ReflectionTestUtils.getField(mappings.iterator().next(), "childs")).size());
	    for(final Mapping child : (Collection<Mapping>) ReflectionTestUtils.getField(mappings.iterator().next(), "childs")) {
	    	
	    	Assert.assertEquals(1, ((Collection<?>)ReflectionTestUtils.getField(child, "paths")).size());
	    	final Object field = ReflectionTestUtils.getField(child, "field");
			
	        Assert.assertEquals(field, ((Collection<?>)ReflectionTestUtils.getField(child, "paths")).iterator().next());
	        Assert.assertTrue(field.equals("value")||(field.equals("key")));
	    	
	    	Assert.assertNull(ReflectionTestUtils.getField(child, "key"));
	    	Assert.assertTrue(((Collection<?>)ReflectionTestUtils.getField(child, "childs")).isEmpty());
	    	
	    }
		
		
	}
	@SuppressWarnings("unchecked")
	private Collection<Mapping> mappingsField(final MapBasedResponse mapBasedResponse) {
		return  (Collection<Mapping>) ReflectionTestUtils.getField(mapBasedResponse, "mappings");
	}
	@Test
	@SuppressWarnings("unchecked")
	public final void mapOtherParentField(){
		final MapBasedResponse mapBasedResponse = new AbstractMapBasedResult(){

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void configure() {
				mappings.add(new Mapping("paging", "info", "start"));
				
			}};
			
		
			final Collection<Mapping> mappings =  mappingsField(mapBasedResponse);
			Assert.assertEquals(1, mappings.size());
			Assert.assertEquals(0,  ((Collection<Mapping>) ReflectionTestUtils.getField(mappings.iterator().next(), "childs")).size());
			
			Assert.assertEquals(1, ((Collection<?>)ReflectionTestUtils.getField(mappings.iterator().next(), "paths")).size());
	    	Assert.assertEquals("start", ((Collection<?>)ReflectionTestUtils.getField(mappings.iterator().next(), "paths")).iterator().next());
	    	
	    	Assert.assertEquals("info", (ReflectionTestUtils.getField(mappings.iterator().next(), "field")));
	    	Assert.assertEquals("paging", (ReflectionTestUtils.getField(mappings.iterator().next(), "key")));
		
	}
	@Test
	public final void rows() {
		final MapBasedResponse mapBasedResponse = new BasicMapBasedResult();
		final Collection<MapBasedResultRow> rows = new ArrayList<>();
		final MapBasedResultRow row = Mockito.mock(MapBasedResultRow.class);
		rows.add(row);
		ReflectionTestUtils.setField(mapBasedResponse, "results", rows);
		Assert.assertEquals(1,  mapBasedResponse.rows().size());
		Assert.assertEquals(rows, mapBasedResponse.rows());
	}
	
	@Test
	public final void single() {
		final MapBasedResponse mapBasedResponse = new BasicMapBasedResult();
	
		final Collection<MapBasedResultRow> rows = rowField(mapBasedResponse);
		final MapBasedResultRow row = new SimpleMapBasedResultRowImpl();
		ReflectionTestUtils.setField(row, "value", SINGLE_VALUE);
		rows.add( row);
		final Collection<Long> results = mapBasedResponse.single(Long.class);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(Long.valueOf(SINGLE_VALUE), results.iterator().next());
	}
	@SuppressWarnings("unchecked")
	private Collection<MapBasedResultRow> rowField(final MapBasedResponse mapBasedResponse) {
		
		return  (Collection<MapBasedResultRow>) ReflectionTestUtils.getField(mapBasedResponse, "results");
	}
	@Test
	public final void composed() {
		final MapBasedResponse mapBasedResponse = new BasicMapBasedResult();
		final Collection<MapBasedResultRow> rows = rowField(mapBasedResponse);
		final Map<String,Object> values = new HashMap<>();
		values.put("id", SINGLE_VALUE);
		final MapBasedResultRow row = new SimpleMapBasedResultRowImpl();
		ReflectionTestUtils.setField(row, "value", values);
		rows.add(row);
		final Collection<Map<String,Object>> results = mapBasedResponse.composed();
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(values, results.iterator().next());
	}
	
	@Test
	public final void composedClass() {
		final MapBasedResponse mapBasedResponse = new BasicMapBasedResult();
		final Collection<MapBasedResultRow> rows = rowField(mapBasedResponse);
		final Map<String,Object> values = new HashMap<>();
		values.put("unit", UNIT);
		values.put("quality", QUALITY);
		final MapBasedResultRow row = new SimpleMapBasedResultRowImpl();
		ReflectionTestUtils.setField(row, "value", values);
		rows.add(row);
		final Collection<PetPriceKey> results = mapBasedResponse.composed(PetPriceKey.class);
		Assert.assertEquals(1, results.size());
		final PetPriceKey result = results.iterator().next();
		Assert.assertEquals(QUALITY, result.quality);
		Assert.assertEquals(UNIT, result.unit);
	}
	

	@Test
	public final void put() {
		final BasicMapBasedResult mapBasedResponse = new BasicMapBasedResult() ;
		
		final Collection<Mapping> mappings = mappingsField(mapBasedResponse);
		mappings.clear();
		
		final Mapping mapping = Mockito.mock(Mapping.class);
		mappings.add(mapping);
		
		final Map<?,?> values = new HashMap<>();
		final Collection<MapBasedResultRow> rows = new ArrayList<>();
		MapBasedResultRow mapBasedResultRow = Mockito.mock(MapBasedResultRow.class);
		rows.add(mapBasedResultRow);
		Mockito.when(mapping.map(mapBasedResponse, SimpleMapBasedResultRowImpl.class, "rows", values)).thenReturn(rows);
		
		mapBasedResponse.put("rows", values);
		final Collection<MapBasedResultRow> results = mapBasedResponse.rows();
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(rows, results);
		
			
	}
	
	@Test
	public final void result() {
		final BasicMapBasedResult mapBasedResult = new BasicMapBasedResult() ;
		final MapBasedResultRow row = Mockito.mock(MapBasedResultRow.class);
		final Map<String,Object> prices = new HashMap<>();
		prices.put("quality", QUALITY);
		prices.put("unit", UNIT);
		
		Mockito.when(row.composedValue()).thenReturn(prices);
		rowField(mapBasedResult).add(row);
		
		
		@SuppressWarnings("rawtypes")
		final List<Map> results =  mapBasedResult.result(Map.class);
		Assert.assertEquals(1,results.size());
		Assert.assertEquals(prices, results.iterator().next());
	}
	
	@Test
	public final void resultClass() {
		final BasicMapBasedResult mapBasedResult = new BasicMapBasedResult() ;
		final MapBasedResultRow row = Mockito.mock(MapBasedResultRow.class);
		PetPriceKey petPriceKey = Mockito.mock(PetPriceKey.class);
		
		Mockito.when(row.composedValue(PetPriceKey.class)).thenReturn(petPriceKey);
		rowField(mapBasedResult).add(row);
	
		final List<PetPriceKey> results =  mapBasedResult.result(PetPriceKey.class);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(petPriceKey, results.iterator().next());
	}
	
	@Test
	public final void resultString() {
		final BasicMapBasedResult mapBasedResult = new BasicMapBasedResult() ;
		final MapBasedResultRow row = Mockito.mock(MapBasedResultRow.class);
		Mockito.when(row.singleValue(String.class)).thenReturn(QUALITY);
		rowField(mapBasedResult).add(row);
		
		final Collection<String> results =  mapBasedResult.result(String.class);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(QUALITY, results.iterator().next());
	}
	
	
	@Test
	public final void assignMapping() {
		final BasicMapBasedResult mapBasedResult = new BasicMapBasedResult() ;
		Collection<Mapping> mappings = new ArrayList<>();
		final Mapping mapping = Mockito.mock(Mapping.class);
		mappings.add(mapping);
		mapBasedResult.assign(mappings);
		
		@SuppressWarnings("unchecked")
		final Collection<Mapping> results = (Collection<Mapping>) ReflectionTestUtils.getField(mapBasedResult, "mappings");
	    Assert.assertEquals(1, results.size());
	    Assert.assertEquals(mapping, results.iterator().next());
	}
	
	@Test
	public final void field() {
		final BasicMapBasedResult mapBasedResult = new BasicMapBasedResult() ;
		ReflectionTestUtils.setField(mapBasedResult, InfoField.Status.field(), STATUS_OK);
		
		Assert.assertEquals(STATUS_OK, mapBasedResult.field(InfoField.Status, String.class));
		
	}
	
	@Test
	public final void fieldClass(){
		final BasicMapBasedResult mapBasedResult = new BasicMapBasedResult() ;
		final Map<String,Object> values = new HashMap<>();
		values.put("quality", QUALITY);
		values.put("unit", UNIT);
		ReflectionTestUtils.setField(mapBasedResult, InfoField.Info.field(), values);
		final PetPriceKey result = mapBasedResult.field(InfoField.Info, PetPriceKey.class);
		Assert.assertEquals(QUALITY, result.quality);
		Assert.assertEquals(UNIT, result.unit);
		
	}
	
	
	
	
	
	
	class BasicMapBasedResult extends AbstractMapBasedResult {

		private static final long serialVersionUID = 1L;
		final String message="";
	

		@Override
		protected void configure() {
			
			final Mapping parent = new Mapping("rows", null);
			final Collection<Mapping> childs = new ArrayList<>();
			childs.add(new Mapping(null, "value", "value"));
			childs.add(new Mapping(null, "key", "key"));
			parent.assignChilds(childs);
			
			mappings.add(parent);
			//assignChildRowMapping(parent, "id", "id");
			//assignParentFieldMapping("total_rows",  "info" );
			//assignParentFieldMapping("offset",  "description" );
			
		}
		
	}
	
	

}
