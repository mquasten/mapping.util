package de.mq.mapping.util.json.support;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

public class Mapping<T>  {
	
	
	final Set<Mapping<T>> childs = new HashSet<>();
	
	
	private  final  String key ;
	
	private final String field;
	
	private List<String> paths = new ArrayList<>();
	

	
	public Mapping(final String key, final String field, final String ... paths) {
		this.key=key;
		this.field=field;
		//this.clazz=clazz;
		assignPaths(paths);
	}

	private void assignPaths(final String... paths) {
		for(String path : paths){
			this.paths.add(path);
		}
	}
	
	public Mapping(final Mapping<T> parent,  final String field, final String ... paths) {
		this.key=null;
		this.field=field;
		assignPaths(paths);
		parent.childs.add(this);
	}
   
	private boolean matchesForParent(final String key) {
		
		Assert.notNull(key, "Key is mandatory");
		Assert.notNull(this.key, "Mapping.key is mandatory");
		return this.key.equals(key);
	}
	
	
	
	private boolean hasField() {
		return (field!=null);
	}
	
	private final Object valueFor(final Object value) {
		Object result = value;
		for(final String path: paths ){
			if (result instanceof Map) {
				
				result=(((Map<?,?>) result).get(path));
				continue;
			}
			if( result == null){
				break;
			}
			
			throw new IllegalArgumentException("Value should be a Map, property " +  path  + " can not be resolved");
		}
		if( field != null ) {
			return result;
		}
		if (result instanceof Collection<?>) {
			return Collections.unmodifiableCollection((Collection<?>) result);
			
		}
		
		final Collection<Object> results = new ArrayList<>();
			
		results.add(result);
		return Collections.unmodifiableCollection(results);
			
		
		
	}



	private void assignField(final Class<?> clazz, final Object target, final Object value) {
		Assert.notNull(field,"Field isn't defined" );
		
		final Field targetField = ReflectionUtils.findField(clazz, field);
		Assert.notNull(targetField, "Field " + field + " not found for Class: " + clazz );
		
		targetField.setAccessible(true);
		
		ReflectionUtils.setField(targetField, target, valueFor(value));
		
	}
	
	
	private void mapRow(final Class<?> clazz, final Object row, final Object result) {
		    Assert.isNull(key,"Key must be empty");
			this.assignField(clazz, result, row);	
		
	}
	
	
	
	
	

	private T mapSubRow(final Class<? extends T> clazz, final Object row /*, final Object result*/) {
		final T result = newRow((Class<? extends T>) clazz);
		for (final Mapping<T>  child : childs) {
			child.mapRow(clazz, row, result);
		}

		return result;
	}


   private Collection<T> mapSubRows(final Class<? extends T> rowClass, final Collection<Map<String, Object>> rows ) {
		final Collection<T> results = new ArrayList<>();
		for (final Object row : rows) {
			//final T result = newRow();
			final T result =  mapSubRow(rowClass, row);
			results.add(result);
		}
		return results;
	}

private T newRow(final Class<? extends T> clazz)  {
	
		return BeanUtils.instantiateClass(clazz);

}
	
	@SuppressWarnings("unchecked")
	public Collection<T> map(final Object parent, final Class<? extends T> rowClass, final String key, final Object value) {
	
		if (!matchesForParent(key)) {
		  return new ArrayList<>(); 
		} 
		
		if (! hasField()) {
			
			return  mapSubRows( rowClass , (Collection<Map<String, Object>>) valueFor(value));
			
		}
        this.assignField(parent.getClass(), parent , value); 
        return new ArrayList<>();
	}
	
}
