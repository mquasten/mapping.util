package de.mq.mapping.util.proxy.support;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.ReflectionUtils;

import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.mapping.util.proxy.NoConverter;
import de.mq.mapping.util.proxy.NoModel;

class ModelRepositoryImpl implements ModelRepository {
	
	private final Map<Key, Object> modelItems = new HashMap<Key,Object>();
	
	private final BeanResolver beanResolver;
	
	
	 ModelRepositoryImpl(final BeanResolver beanResolver, final Object ... models) {
		this.beanResolver=beanResolver;
		
		for(final Object obj : models){
			
			modelItems.put(new KeyImpl(obj.getClass()),   obj );
		}
			
	}

	
	
     ModelRepositoryImpl(final BeanResolver beanResolver,final Map<String,? extends Object> map, final Object ... models) {
		this(beanResolver, models);
		for(final Entry<String, ? extends Object> enty : map.entrySet()){
			modelItems.put(new KeyImpl(enty.getKey()), enty.getValue());
		}
			
	}
	
	

	
	/* (non-Javadoc)
	 * @see de.mq.util.proxy.support.ModelRepository#put(java.lang.Object)
	 */
	public final void put(final Object value) {
		put(value.getClass(),null,value, NoConverter.class);
	     	
	}
	
	/* (non-Javadoc)
	 * @see de.mq.util.proxy.support.ModelRepository#put(java.lang.Class, java.lang.String, java.lang.Object, java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public final void put(final Class<?> clazz, final String field, final Object value, @SuppressWarnings("rawtypes") final Class<? extends Converter> converterClass ){
		if( field == null){
			addParent(clazz, value);
			return;
		}
		
		
		if (clazz.equals(NoModel.class)){
			modelItems.put(new KeyImpl(field), value);
			return;
		}
		
		final Key key= new KeyImpl(clazz);
		domainObjectNotFoundGuard(value.getClass(), key);
		
		final Object domain = modelItems.get(key);
		final Key errorKey = new KeyImpl(clazz,field);
		modelItems.remove(errorKey);
		try {
			
			final Field reflectionField = ReflectionUtils.findField(domain.getClass(), field);
			
			reflectionField.setAccessible(true);
			
			ReflectionUtils.setField(reflectionField, domain,  beanResolver.getBeanOfType(converterClass).convert(value));
			
		} catch(final IllegalArgumentException iae){
			modelItems.put(errorKey, value);
		}
	}

	
	
	
	
	private void addParent(final Class<?> clazz, final Object value) {
		if (value instanceof Map<?,?>) {
			addMap(value);
			
			return;
		}
		deleteChilds(clazz);
		modelItems.put(new KeyImpl(value.getClass()), value);
	}
	@SuppressWarnings("unchecked")
	private void addMap(final Object value) {
		for(Entry<String,Object> entry : ((Map<String, Object>) value).entrySet()){
			modelItems.put(new KeyImpl(entry.getKey()), entry.getValue());
		}
	}

	void deleteChilds(final Class<?> clazz) {
		for(final Key key : childKeys(clazz)) {
			modelItems.remove(key);
		}
	}

	final Set<Key> childKeys(final Class<?> clazz) {
		final Set<Key> keys = new HashSet<Key>();
		for(Key key : modelItems.keySet()){
			if ( !key.hasParent(clazz)){
				continue;
			}
			keys.add(key);
		}
		return Collections.unmodifiableSet(keys);
	}

	private void domainObjectNotFoundGuard(final Class<?> clazz, final Key key) {
		if( ! modelItems.containsKey(key)){
			throw new IllegalArgumentException("No domain object found for type " + clazz);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.util.proxy.support.ModelRepository#get(java.lang.Class)
	 */
	public final Object get(final Class<?> clazz) {
		return get(clazz,null);
	}
	
	/* (non-Javadoc)
	 * @see de.mq.util.proxy.support.ModelRepository#get(java.lang.Class, java.lang.String)
	 */
	public final Object get(final Class<?> clazz, final String name) {
		
		if( name == null){
			
			return parent(clazz);
		}
		
		if( clazz.equals(NoModel.class)){
			return modelItems.get(new KeyImpl(name));
		}
		
		final Key errorKey = new KeyImpl(clazz, name);
		if ( modelItems.containsKey(errorKey)){
			return modelItems.get(errorKey);
		}
		
		final Object domain = modelItems.get(new KeyImpl(clazz));
		
		final Field field = ReflectionUtils.findField(domain.getClass(), name);
		
		try {
			field.setAccessible(true);
			
			return field.get(domain);
		} catch (final Exception ex) {
			
			throw new IllegalStateException("Field " + name , ex );
		} 
		
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Object get(Class<?> clazz, String name, @SuppressWarnings("rawtypes") Class<? extends Converter> converter, Class<?> resultType) {
		final Object result = get(clazz, name);
		
			if( resultType.isInstance(result)) {
				return result;
			}
			
			return beanResolver.getBeanOfType(converter).convert(result);
		
	} 

	private Object parent(final Class<?> clazz) {
		if( clazz==NoModel.class ) {
			return mapParent();
		}
			
		
		final Key key = new KeyImpl(clazz);
		domainObjectNotFoundGuard(clazz, key);
		return modelItems.get(key);
	}

	private Map<String, Object> mapParent() {
		final Map<String,Object> results = new HashMap<String,Object>();
		for(final Entry<Key, Object> entry: modelItems.entrySet()){
			if( ! entry.getKey().isMapKey() ) {
				continue;
			}
			results.put(entry.getKey().name(), entry.getValue());
		}
		return results;
	}

	@Override
	public final  boolean  hasError(Class<?> clazz, final String field) {
		return modelItems.containsKey(new KeyImpl(clazz, field));
	}



	@Override
	public BeanResolver beanResolver() {
		return this.beanResolver;
	}

	
	

}
