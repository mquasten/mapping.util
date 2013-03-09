package de.mq.mapping.util.proxy;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodCallback;


public final  class ProxyUtil {
	
	public final static  <T> Set<T>  collectDomains(final Class<T> domainClass, final Object proxy) {
		final Set<T> results = new HashSet<>();
		ReflectionUtils.doWithMethods(proxy.getClass(), new MethodCallback() {
            
		 	@Override
			public void doWith(final Method method) throws IllegalArgumentException, IllegalAccessException {
		 		
				if( ! method.isAnnotationPresent(GetterDomain.class)){
					return;
				}
				method.setAccessible(true);
				
				@SuppressWarnings("unchecked")
				final T domain = (T) invoke(proxy, method);
				
				if ( ! domainClass.isInstance(domain)) {
					return;
				}
				
		        results.add(domain);
			}});
		    return results;
	}

	
	private static Object invoke(final Object object, final Method method) {
		try {
			return  method.invoke(object);
		} catch (final Exception ex ) {
			throw new IllegalStateException(ex);
		}
	}

}
