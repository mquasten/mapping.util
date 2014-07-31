package de.mq.mapping.util.json.support;

import java.util.Map;

public interface MapCopyOperations {

	public abstract <T> T createShallowCopyFieldsFromMap(Class<? extends T> targetClass, Map<String, ? extends Object> values);

	public abstract <T> void shallowCopyFields(Map<String, ? extends Object> values, T target);

}