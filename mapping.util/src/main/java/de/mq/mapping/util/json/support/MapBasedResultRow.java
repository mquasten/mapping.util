package de.mq.mapping.util.json.support;

import java.util.Collection;
import java.util.Map;

public interface MapBasedResultRow {

	String id();

	<T> T singleValue(Class<? extends T> clazz);

	<T> T singleKey(Class<? extends T> clazz);

	Map<String, Object> composedKey();

	Map<String, Object> composedValue();
	
	<T> T composedValue(final Class<? extends T> targetClass);
	
	<T> T composedKey(final Class<? extends T> targetClass) ;

	<T> Collection<T> collectionKey(Class<? extends T> targetClass);

	<T> Collection<T> collectionValue(Class<? extends T> targetClass);

	

	

}