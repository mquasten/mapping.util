package de.mq.mapping.util.json.support;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodCallback;
import org.springframework.util.StringUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import de.mq.mapping.util.json.FieldMapping;

public class MapCopyTemplate implements MapCopyOperations {

	/* (non-Javadoc)
	 * @see de.mq.merchandise.util.chouchdb.support.MapCopyOperations#createShallowCopyFieldsFromMap(java.lang.Class, java.util.Map)
	 */
	@Override
	public <T> T createShallowCopyFieldsFromMap(final Class<? extends T> targetClass, final Map<String, ? extends Object> values) {
		final T target = BeanUtils.instantiateClass(targetClass);
		shallowCopyFields(values, target);
		return target;
	}

	/* (non-Javadoc)
	 * @see de.mq.merchandise.util.chouchdb.support.MapCopyOperations#shallowCopyFields(java.util.Map, T)
	 */
	@Override
	public <T> void shallowCopyFields(final Map<String, ? extends Object> values, final T target) {
		
		ReflectionUtils.doWithFields(target.getClass(), new FieldCallback() {

			@Override
			public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
				if (!field.isAnnotationPresent(FieldMapping.class)) {
					return;
				}

				String name = field.getName();
				if (StringUtils.hasText(field.getAnnotation(FieldMapping.class).value())) {
					name = field.getAnnotation(FieldMapping.class).value();
				}

				if (!(values.containsKey(name))) {
					return;
				}
				@SuppressWarnings("unchecked")
				final Converter<Object, Object> converter = (Converter<Object, Object>) BeanUtils.instantiateClass(field.getAnnotation(FieldMapping.class).converter());
				field.setAccessible(true);

				field.set(target, converter.convert(values.get(name)));

			}
		});
		
		ReflectionUtils.doWithMethods(target.getClass(), new MethodCallback() {

			@Override
			public void doWith(final Method method) throws IllegalArgumentException, IllegalAccessException {
				if( ! method.isAnnotationPresent(FieldMapping.class)) {
					return;
				}
				final String name = method.getAnnotation(FieldMapping.class).value();
				@SuppressWarnings("unchecked")
				final Converter<Object, Object> converter = (Converter<Object, Object>) BeanUtils.instantiateClass(method.getAnnotation(FieldMapping.class).converter());
				try {
					method.invoke(target, new Object[] {converter.convert(values.get(name))});
				} catch (final InvocationTargetException ex) {
					ReflectionUtils.handleInvocationTargetException(ex);
				}
				
				
			} } );
	}

}
