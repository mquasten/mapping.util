package de.mq.mapping.util.json.support;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.json.support.MapBasedResultRow;



public class SimpleMapBasedResultRowTest {
	
	
	private static final String UNIT = "private date";
	private static final String QUALITY = "platinium";
	static final String ID = "19680528";

	@Test
	public final void id() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();
		ReflectionTestUtils.setField(mapBasedResultRow, "id",ID);
		Assert.assertEquals(ID, mapBasedResultRow.id());
	}
	
	@Test
	public final void singleRow() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();
		ReflectionTestUtils.setField(mapBasedResultRow, "key", ID);
		Assert.assertEquals(Long.valueOf(ID), mapBasedResultRow.singleKey(Long.class));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void singleKeyWithMap() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();
		ReflectionTestUtils.setField(mapBasedResultRow, "key", new HashMap<>());
		mapBasedResultRow.singleKey(Map.class);
	}
	
	@Test
	public final void singleValue() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();
		ReflectionTestUtils.setField(mapBasedResultRow, "value", ID);
		Assert.assertEquals(Long.valueOf(ID), mapBasedResultRow.singleValue(Long.class));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void singleValueWithMap() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();
		ReflectionTestUtils.setField(mapBasedResultRow, "value", new HashMap<>());
		mapBasedResultRow.singleValue(Map.class);
	}
	
	@Test
	public final void composedKey() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();
		final HashMap<Object, Object> values = new HashMap<>();
		values.put("key" , ID);
		ReflectionTestUtils.setField(mapBasedResultRow, "key", values);
		
		Assert.assertEquals(values, mapBasedResultRow.composedKey());
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public final void composedKeyNotMap() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();
		ReflectionTestUtils.setField(mapBasedResultRow, "key", ID);
		mapBasedResultRow.composedKey();
	}
	
	@Test
	public final void composedValue() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();
		final HashMap<Object, Object> values = new HashMap<>();
		values.put("value" , ID);
		ReflectionTestUtils.setField(mapBasedResultRow, "value", values);
		Assert.assertEquals(values, mapBasedResultRow.composedValue());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void composedValueNotMap() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();
		ReflectionTestUtils.setField(mapBasedResultRow, "value", ID);
		mapBasedResultRow.composedValue();
	}
	
	@Test
	public final void composedKeyObject() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();
		final HashMap<Object, Object> keys = new HashMap<>();
		keys.put("quality" , QUALITY);
		keys.put("unit" , UNIT);
		ReflectionTestUtils.setField(mapBasedResultRow, "key", keys);
		Assert.assertEquals(QUALITY, mapBasedResultRow.composedKey(PetPriceKey.class).quality);
		Assert.assertEquals(UNIT, mapBasedResultRow.composedKey(PetPriceKey.class).unit);
	}
	
	@Test
	public final void composedValueobject() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();
		final HashMap<Object, Object> keys = new HashMap<>();
		keys.put("quality" , QUALITY);
		keys.put("unit" , UNIT);
		ReflectionTestUtils.setField(mapBasedResultRow, "value", keys);
		Assert.assertEquals(QUALITY, mapBasedResultRow.composedValue(PetPriceKey.class).quality);
		Assert.assertEquals(UNIT, mapBasedResultRow.composedValue(PetPriceKey.class).unit);
	}
	
	

}
