package de.mq.mapping.util.proxy.support;

import java.lang.reflect.Method;
import java.util.UUID;

import org.springframework.core.convert.converter.Converter;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.GetterProxy;
import de.mq.mapping.util.proxy.ModelRepository;

class BasicGetterProxyInterceptorImpl implements Interceptor {

	private final ModelRepository modelRepository;
	
	public BasicGetterProxyInterceptorImpl(final ModelRepository modelRepository) {
		this.modelRepository=modelRepository;
	
	}

	
	@SuppressWarnings({ "unchecked" })
	public final Object invoke(final Method method, final Object[] args) throws Throwable {
		annotationAwareGuard(method);
		final UUID uuid =UUID.nameUUIDFromBytes(method.getAnnotation(GetterProxy.class).name().getBytes());
		if( modelRepository.isCached(method.getAnnotation(GetterProxy.class).clazz(), uuid, null)) {
			return modelRepository.get(method.getAnnotation(GetterProxy.class).clazz(), uuid);
		}
		final AOProxyFactory factory = modelRepository.beanResolver().getBeanOfType(AOProxyFactory.class);
		Object value = modelRepository.get(method.getAnnotation(GetterProxy.class).clazz(), method.getAnnotation(GetterProxy.class).name());
		
		if (value == null) {
			return null;
		}
		
		
		value=((Converter<Object, Object>) modelRepository.beanResolver().getBeanOfType(method.getAnnotation(GetterProxy.class).converter())).convert(value);
	

		
		
		
		
		
		
	    final Object result =  factory.createProxy(proxyClass(method.getAnnotation(GetterProxy.class).proxyClass(), value), new ModelRepositoryImpl(modelRepository.beanResolver(), value));
	 
	  
	    modelRepository.put(method.getAnnotation(GetterProxy.class).clazz(), uuid, result);
	
	    
	    return result; 
	}
	
	
	
  
	@SuppressWarnings("unchecked")
	private Class<? extends Object> proxyClass(final Class<? extends Object> clazz, final Object value) {
		if( ! Converter.class.isAssignableFrom(clazz)) {
			return clazz;
		}
		return  ((Converter<Object, Class<?>>) modelRepository.beanResolver().getBeanOfType(clazz)).convert(value);
		
            
    }
	
	private void annotationAwareGuard(final Method method) {
		if( ! method.isAnnotationPresent(GetterProxy.class) ) {
			throw new IllegalStateException("Method " + method.getName() + " should be annotated with " + GetterProxy.class.getSimpleName());
		}
	}
	

}
