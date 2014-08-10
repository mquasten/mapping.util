package de.mq.mapping.util.json.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.json.support.MapBasedResponse;
import de.mq.mapping.util.json.support.MapBasedResultRow;

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
		final Mapping mapping = new Mapping(KEY_VALUE, FIELD_VALUE, PATH_VALUE_FIRST, PATH_VALUE_NEXT);

		Assert.assertEquals(KEY_VALUE, ReflectionTestUtils.getField(mapping, KEY_FIELD_NAME));
		Assert.assertEquals(FIELD_VALUE, ReflectionTestUtils.getField(mapping, FIELD_FIELD_NAME));
		final List<String> paths = pathsField(mapping);
		Assert.assertEquals(2, paths.size());
		Assert.assertEquals(PATH_VALUE_FIRST, paths.get(0));
		Assert.assertEquals(PATH_VALUE_NEXT, paths.get(1));
	}

	@SuppressWarnings("unchecked")
	private List<String> pathsField(final Mapping mapping) {
		return (List<String>) ReflectionTestUtils.getField(mapping, PATHS_FIELD_NAME);
	}

	@Test
	public final void field() {
		final Mapping mapping = new Mapping("hotScore", "info", "hotScore");
		final MapBasedResponse parent = new BasicMapBasedResult();
		final Map<String, Object> values = new HashMap<>();
		values.put("hotScore", 10);
		mapping.map(parent, null, "hotScore", values);
		Assert.assertEquals(10, ReflectionTestUtils.getField(parent, "info"));
	}

	@Test(expected = IllegalArgumentException.class)
	public final void fieldNotFound() {
		final Mapping mapping = new Mapping("hotScore", "xxx", "hotScore");

		final Map<String, Object> values = new HashMap<>();
		values.put("hotScore", 10);
		mapping.map(new BasicMapBasedResult(), null, "hotScore", values);
	}

	@Test
	public final void rows() {
		Mapping parent = new Mapping("rows", null);
		final Collection<Mapping> childs = new ArrayList<>();
		childs.add(new Mapping(null, "value"));
		parent.assignChilds(childs);
		MapBasedResponse mapBasedResponse = new BasicMapBasedResult();
		final Collection<Map<String, Object>> rows = listResult();

		final Collection<MapBasedResultRow> results = parent.map(mapBasedResponse, SimpleMapBasedResultRowImpl.class, "rows", rows);

		final Iterator<Map<String, Object>> it = rows.iterator();
		for (MapBasedResultRow result : results) {
			Assert.assertEquals(it.next(), result.composedValue());
		}

	}

	private Collection<Map<String, Object>> listResult() {
		final Collection<Map<String, Object>> rows = new ArrayList<>();
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
		final Mapping parent = new Mapping("rows", null);
		final Collection<Mapping> childs = new ArrayList<>();
		childs.add(new Mapping(null, "value"));
		parent.assignChilds(childs);

		final Map<String, Object> row = new HashMap<>();
		row.put("name", "Nicole");
		row.put("quality", "Platinium");
		row.put("unit", "date");
		MapBasedResponse mapBasedResponse = new BasicMapBasedResult();
		final Collection<MapBasedResultRow> results = parent.map(mapBasedResponse, SimpleMapBasedResultRowImpl.class, "rows", row);
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(row, results.iterator().next().composedValue());

	}

	@Test(expected = IllegalArgumentException.class)
	public final void notMatchesForParent() {
		Mapping parent = new Mapping("row", null);
		ReflectionTestUtils.setField(parent, "key", null);
		Assert.assertTrue(parent.map(new BasicMapBasedResult(), SimpleMapBasedResultRowImpl.class, "rows", new HashMap<String, Object>()).isEmpty());
	}

	@Test(expected = IllegalArgumentException.class)
	public final void notMatchesForRow() {
		final Mapping parent = new Mapping("rows", null);
		final Collection<Mapping> childs = new ArrayList<>();

		final Mapping child = new Mapping(null, "value");
		childs.add(child);
		parent.assignChilds(childs);

		ReflectionTestUtils.setField(child, "key", "name");
		parent.map(new BasicMapBasedResult(), SimpleMapBasedResultRowImpl.class, "rows", listResult());

	}

	@Test
	public final void rowNoMap() {
		final Mapping parent = new Mapping("rows", null, "artist");
		final Collection<Mapping> childs = new ArrayList<>();
		childs.add(new Mapping(null, "value"));
		parent.assignChilds(childs);
		final Map<String, Object> row = new HashMap<>();
		row.put("artist", "Kylie");
		final Collection<MapBasedResultRow> results = parent.map(new BasicMapBasedResult(), SimpleMapBasedResultRowImpl.class, "rows", row);
		Assert.assertEquals(1, results.size());
		for (final MapBasedResultRow result : results) {
			Assert.assertEquals("Kylie", result.singleValue(String.class));

		}
	}

	@Test(expected = IllegalArgumentException.class)
	public final void wrongPrperty() {
		Mapping parent = new Mapping("rows", null, "artist", "name");
		final Collection<Mapping> childs = new ArrayList<>();
		childs.add(new Mapping(null, "value"));
		parent.assignChilds(childs);
		final Map<String, Object> row = new HashMap<>();
		row.put("artist", "Kylie");
		parent.map(new BasicMapBasedResult(), SimpleMapBasedResultRowImpl.class, "rows", row);
	}

	@Test()
	public final void propertyNull() {
		Mapping parent = new Mapping("rows", null, "artist", "name");
		final Collection<Mapping> childs = new ArrayList<>();
		childs.add(new Mapping(null, "value"));
		parent.assignChilds(childs);
		parent.map(new BasicMapBasedResult(), SimpleMapBasedResultRowImpl.class, "rows", new HashMap<>());
		final Collection<MapBasedResultRow> results = parent.map(new BasicMapBasedResult(), SimpleMapBasedResultRowImpl.class, "rows", new HashMap<>());
		Assert.assertEquals(1, results.size());
		for (final MapBasedResultRow result : results) {
			Assert.assertNull(result.singleValue(String.class));
		}

	}

	@Test()
	public final void parentNotMatch() {
		Mapping parent = new Mapping("rows", null);
		final Collection<Mapping> childs = new ArrayList<>();
		childs.add(new Mapping(null, "value"));
		parent.assignChilds(childs);

		MapBasedResponse mapBasedResponse = new BasicMapBasedResult();
		Assert.assertTrue(parent.map(mapBasedResponse, SimpleMapBasedResultRowImpl.class, "dontLetMeGetMe", listResult()).isEmpty());

	}

	@Test()
	public final void key() {
		Assert.assertEquals(FIELD_VALUE, new Mapping(null, FIELD_VALUE).key());
		Assert.assertEquals(Mapping.PARENT_FIELDNAME, new Mapping("rows", null).key());
	}
	@Test()
	public final void equalsNoMapping() {
		final Mapping mapping =new Mapping("rows", null);
		Assert.assertFalse(mapping.equals(new Date()));
	}
	
	@Test()
	public final void equals() {
		final Mapping mapping =new Mapping("rows", null);
		Assert.assertTrue(mapping.equals(new Mapping("rows", null)));
	}

	class BasicMapBasedResult extends AbstractMapBasedResult {

		private static final long serialVersionUID = 1L;

		@Override
		protected void configure() {

		}

	}
}
