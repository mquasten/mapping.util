package de.mq.mapping.util.json.support;

import java.util.ArrayList;
import java.util.Collection;
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
		ReflectionTestUtils.setField(mapBasedResultRow, "id", ID);
		Assert.assertEquals(ID, mapBasedResultRow.id());
	}

	@Test
	public final void singleRow() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();
		ReflectionTestUtils.setField(mapBasedResultRow, "key", ID);
		Assert.assertEquals(Long.valueOf(ID), mapBasedResultRow.singleKey(Long.class));
	}

	@Test(expected = IllegalArgumentException.class)
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

	@Test(expected = IllegalArgumentException.class)
	public final void singleValueWithMap() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();
		ReflectionTestUtils.setField(mapBasedResultRow, "value", new HashMap<>());
		mapBasedResultRow.singleValue(Map.class);
	}

	@Test
	public final void composedKey() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();
		final HashMap<Object, Object> values = new HashMap<>();
		values.put("key", ID);
		ReflectionTestUtils.setField(mapBasedResultRow, "key", values);

		Assert.assertEquals(values, mapBasedResultRow.composedKey());
	}

	@Test(expected = IllegalArgumentException.class)
	public final void composedKeyNotMap() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();
		ReflectionTestUtils.setField(mapBasedResultRow, "key", ID);
		mapBasedResultRow.composedKey();
	}

	@Test
	public final void composedValue() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();
		final HashMap<Object, Object> values = new HashMap<>();
		values.put("value", ID);
		ReflectionTestUtils.setField(mapBasedResultRow, "value", values);
		Assert.assertEquals(values, mapBasedResultRow.composedValue());
	}

	@Test(expected = IllegalArgumentException.class)
	public final void composedValueNotMap() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();
		ReflectionTestUtils.setField(mapBasedResultRow, "value", ID);
		mapBasedResultRow.composedValue();
	}

	@Test
	public final void composedKeyObject() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();
		final HashMap<Object, Object> keys = new HashMap<>();
		keys.put("quality", QUALITY);
		keys.put("unit", UNIT);
		ReflectionTestUtils.setField(mapBasedResultRow, "key", keys);
		Assert.assertEquals(QUALITY, mapBasedResultRow.composedKey(PetPriceKey.class).quality);
		Assert.assertEquals(UNIT, mapBasedResultRow.composedKey(PetPriceKey.class).unit);
	}

	@Test
	public final void composedValueobject() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();
		final HashMap<Object, Object> keys = new HashMap<>();
		keys.put("quality", QUALITY);
		keys.put("unit", UNIT);
		ReflectionTestUtils.setField(mapBasedResultRow, "value", keys);
		Assert.assertEquals(QUALITY, mapBasedResultRow.composedValue(PetPriceKey.class).quality);
		Assert.assertEquals(UNIT, mapBasedResultRow.composedValue(PetPriceKey.class).unit);
	}

	@Test
	public final void collectionValueString() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();
		final Collection<Long> values = new ArrayList<>();
		values.add(Long.valueOf(10));
		ReflectionTestUtils.setField(mapBasedResultRow, "value", values);
		Collection<?> results = mapBasedResultRow.collectionValue(String.class);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals("10", results.iterator().next());
	}

	@Test
	public final void collectionValueClass() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();
		final Collection<Map<?, ?>> values = new ArrayList<>();
		Map<String, Object> row = new HashMap<>();
		row.put("quality", QUALITY);
		row.put("unit", UNIT);
		values.add(row);

		ReflectionTestUtils.setField(mapBasedResultRow, "value", values);
		Collection<PetPriceKey> results = mapBasedResultRow.collectionValue(PetPriceKey.class);
		Assert.assertEquals(1, results.size());
		final PetPriceKey result = results.iterator().next();
		Assert.assertEquals(QUALITY, result.quality);

		Assert.assertEquals(UNIT, result.unit);
	}

	@Test
	public final void collectionValueMap() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();
		final Collection<Map<?, ?>> values = new ArrayList<>();
		Map<String, Object> row = new HashMap<>();
		row.put("quality", QUALITY);
		row.put("unit", UNIT);
		values.add(row);
		ReflectionTestUtils.setField(mapBasedResultRow, "value", values);
		@SuppressWarnings("rawtypes")
		final Collection<Map> results = mapBasedResultRow.collectionValue(Map.class);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(QUALITY, results.iterator().next().get("quality"));
		Assert.assertEquals(UNIT, results.iterator().next().get("unit"));

	}

	@Test(expected = IllegalArgumentException.class)
	public final void collectionValueNoCollection() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();

		mapBasedResultRow.collectionValue(Map.class);
	}

	@Test
	public final void collectionKey() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();
		final Collection<String> qualities = new ArrayList<>();
		qualities.add(QUALITY);
		ReflectionTestUtils.setField(mapBasedResultRow, "key", qualities);
		final Collection<String> results = mapBasedResultRow.collectionKey(String.class);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(QUALITY, results.iterator().next());
	}

	@Test(expected = IllegalArgumentException.class)
	public final void collectionKeyNoCollection() {
		final MapBasedResultRow mapBasedResultRow = new SimpleMapBasedResultRowImpl();
		mapBasedResultRow.collectionKey(Map.class);
	}

}
