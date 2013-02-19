package de.mq.mapping.util.proxy;

import org.springframework.core.convert.converter.Converter;


public interface ModelRepository {

	void put(final Object value);

	void put(final Class<?> clazz, final String field, final Object value,@SuppressWarnings("rawtypes") final Class<? extends Converter> converter);

	Object get(final Class<?> clazz);

	Object get(final Class<?> clazz, final String name);
	
	Object get(final Class<?> clazz, final String name,  @SuppressWarnings("rawtypes") final Class<? extends Converter>  converter, final Class<?> resultType );
	
	boolean hasError(final Class<?> clazz, final String field);
	
	BeanResolver beanResolver();

}