package de.mq.mapping.util.json.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.convert.support.DefaultConversionService;



class SimpleMapBasedResultRowImpl implements MapBasedResultRow {

	private Object key;

	private Object value;

	private String id;
	
	SimpleMapBasedResultRowImpl() {
		
	}
	

	final ConfigurableConversionService conversionService = new DefaultConversionService();

	
	private MapCopyOperations mapCopyTemplate = new MapCopyTemplate();

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.order.support.CouchViewResultRow#id()
	 */
	@Override
	public final String id() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.order.support.CouchViewResultRow#singleKey()
	 */
	
	@Override
	public final <T> T singleKey(Class<? extends T> clazz) {

		if (key instanceof Map) {
			throw new IllegalArgumentException("Key is a Composed Key");
		}

		
		return conversionService.convert(key, clazz);
		
		
	}
	
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.order.support.CouchViewResultRow#singleValue()
	 */
	@Override
	public final <T> T singleValue(Class<? extends T> clazz) {
		if (value instanceof Map) {
			throw new IllegalArgumentException("Value is a Composed Key");
		}

		return conversionService.convert(value, clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.order.support.CouchViewResultRow#composedKey()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final Map<String, Object> composedKey() {
		composedKeyGuard();
		return Collections.unmodifiableMap((Map<String, ? extends Object>) key);
	}

	private void composedKeyGuard() {
		if (!(key instanceof Map<?, ?>)) {
			throw new IllegalArgumentException("Key is not composed");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.order.support.CouchViewResultRow#composedValue()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final Map<String, Object> composedValue() {
		composedValueGuard();
		return Collections.unmodifiableMap((Map<String, ? extends Object>) value);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public final  <T> Collection<T> collectionValue(Class<? extends T> targetClass) {
		valueCollectionGuard();
		return (Collection<T>) handleCollectionResult(targetClass, value);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public final  <T> Collection<T> collectionKey(Class<? extends T> targetClass) {
		keyCollectionGuard();
		return (Collection<T>) handleCollectionResult(targetClass, key);
	}

	
	private  Collection<?> handleCollectionResult(Class<?> targetClass, final Object collection) {
	
		if( instanceOfMap(targetClass) ) {
			return Collections.unmodifiableCollection((Collection<?>) collection);
		}
       
        final Collection<Object> results = new ArrayList<>();
        for(final Object row : (Collection< ? >) collection){
        	if( getClass().getClassLoader().equals(targetClass.getClassLoader())) {
        		results.add(row);
        		continue;
        	}
        	results.add(conversionService.convert(row, targetClass));
        }
		
		
		return Collections.unmodifiableCollection(results);
	}

	private void keyCollectionGuard() {
		if (!(key instanceof Collection<?>)) {
			throw new IllegalArgumentException("Key is not  a collection.");
		}
	}

	private void valueCollectionGuard() {
		if (!(value instanceof Collection<?>)) {
			throw new IllegalArgumentException("Value is not  a collection.");
		}
	}

	private void composedValueGuard() {
		if (!(value instanceof Map<?, ?>)) {
			throw new IllegalArgumentException("Value is not  composed.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.mq.merchandise.order.support.CouchViewResultRow#composedValue(java
	 * .lang.Class)
	 */
	@Override
	public final <T> T composedValue(final Class<? extends T> targetClass) {
		composedValueGuard();
		return mapCopyTemplate.createShallowCopyFieldsFromMap(targetClass, composedValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.mq.merchandise.order.support.CouchViewResultRow#composedKey(java.lang
	 * .Class)
	 */
	@Override
	public final <T> T composedKey(final Class<? extends T> targetClass) {
		composedKeyGuard();
		return mapCopyTemplate.createShallowCopyFieldsFromMap(targetClass, composedKey());
	}

	
	
	private <T> boolean instanceOfMap(final Class<? extends T> targetClass) {
		try {
		targetClass.asSubclass(Map.class);
		return true;
		} catch (final ClassCastException ce){
			return false;
		}
	}
	
}
