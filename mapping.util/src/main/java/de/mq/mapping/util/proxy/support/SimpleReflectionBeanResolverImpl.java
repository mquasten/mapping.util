package de.mq.mapping.util.proxy.support;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import de.mq.mapping.util.proxy.BeanResolver;

@Component()
@Profile({"CGLib-Proxy", "Dynamic-Proxy"})
public class SimpleReflectionBeanResolverImpl implements BeanResolver{

	private final  Map<Class<?>, Object> beans = new HashMap<>();
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBeanOfType(final Class<? extends T> clazz) {
		if( ! beans.containsKey(clazz)){
			beans.put(clazz, newBean(clazz));
		}
		
		return (T) beans.get(clazz);
	}

	private <T> T newBean(final Class<? extends T> clazz) {
		try {
			final Constructor<? extends T> constructor = clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		} catch (final Exception ex) {
			 throw new IllegalStateException("Unable to create bean", ex);
		}
	}

	@Override
	public void put(final Class<?> clazz, final Object bean) {
		typeGuard(clazz, bean);
		beans.put(clazz, bean);
	}

	private void typeGuard(Class<?> clazz, Object bean) {
		if( ! clazz.isInstance(bean) ) {
			throw new IllegalArgumentException("Bean has wrong type");
		}
	}

}
