package de.mq.mapping.util.proxy.support;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.core.convert.converter.Converter;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.GetterProxyCollection;
import de.mq.mapping.util.proxy.ModelRepository;

class BasicGetterCollectionInterceptorImpl implements Interceptor {

	final ModelRepository modelRepository;
	
	public BasicGetterCollectionInterceptorImpl(final ModelRepository modelRepository) {
		this.modelRepository=modelRepository;
	}
	
	private void comparatorListGuard(final Collection<Object> results) {
		if (!(results instanceof List<?>)) {
			throw new IllegalArgumentException("Used Collection must be an instance of List, if a comparator is used: " + results.getClass().getName());
			
		}
	}

	private void methodAnnotationGuard(final Method method) {
		if ( ! method.isAnnotationPresent(GetterProxyCollection.class)) {
			throw new IllegalStateException("Method " + method.getName() + " should be annotated with " + GetterProxyCollection.class.getSimpleName());
		}
	}
	
	
	
	@SuppressWarnings("unchecked")
	public final  Object invoke(final Method method, final Object[] args) throws Throwable {
		methodAnnotationGuard(method);
		
		
		
		final Collection<Object> results = method.getAnnotation(GetterProxyCollection.class).collectionClass().newInstance();
		
		final Object value = modelRepository.get(method.getAnnotation(GetterProxyCollection.class).clazz(), method.getAnnotation(GetterProxyCollection.class).name());
		
		final UUID uuid =UUID.nameUUIDFromBytes(method.getAnnotation(GetterProxyCollection.class).name().getBytes());
		
		
		if( modelRepository.isCached(method.getAnnotation(GetterProxyCollection.class).clazz(), uuid, value)) {
			return modelRepository.get(method.getAnnotation(GetterProxyCollection.class).clazz(), uuid);
		}
		
		
		final AOProxyFactory factory=modelRepository.beanResolver().getBeanOfType(AOProxyFactory.class);
		
		if( value == null ){
			modelRepository.put(method.getAnnotation(GetterProxyCollection.class).clazz(), uuid, results);
			return results;
		}
		
		final Converter<Object, Class<?>> converter =  proxyClass( method.getAnnotation(GetterProxyCollection.class).proxyClass());
		
		for(Object member : (Collection<Object>) value){
			
			member=((Converter<Object,Object>) modelRepository.beanResolver().getBeanOfType(method.getAnnotation(GetterProxyCollection.class).converter())).convert(member);
			if ( converter != null){
				results.add(factory.createProxy(converter.convert(member), new ModelRepositoryImpl(modelRepository.beanResolver(), member)));
				continue;
			} 
			results.add(factory.createProxy( method.getAnnotation(GetterProxyCollection.class).proxyClass(), new ModelRepositoryImpl(modelRepository.beanResolver(), member)));
		}
		

		if (method.getAnnotation(GetterProxyCollection.class).comparator().equals(GetterProxyCollection.NoComparator.class)){
			modelRepository.put(method.getAnnotation(GetterProxyCollection.class).clazz(), uuid, results);
			return results;	
		}
		
		comparatorListGuard(results);
		
		
		final Comparator<Object> comparator =  (Comparator<Object>) modelRepository.beanResolver().getBeanOfType(method.getAnnotation(GetterProxyCollection.class).comparator());
		
		Collections.sort((List<?>) results, comparator);
		modelRepository.put(method.getAnnotation(GetterProxyCollection.class).clazz(), uuid, results);
		return results;
	}

	@SuppressWarnings("unchecked")
	private Converter<Object, Class<?>> proxyClass(final Class<?> clazz)  {
		final Object  result = resolveConverter(clazz);
		if (! (result instanceof Converter<?,?>)) {
			 return null;
		}
		return (Converter<Object, Class<?>>) result;
	}

	private Object resolveConverter(final Class<?> clazz) {
		try {
		    return  modelRepository.beanResolver().getBeanOfType(clazz);
		} catch ( Exception ex ){
			return null;
		}
	} 

	

	
	
}
