package de.mq.mapping.util.proxy.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.ExceptionTranslations;
import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.GetterProxy;
import de.mq.mapping.util.proxy.GetterProxyCollection;
import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.mapping.util.proxy.Setter;
import de.mq.mapping.util.proxy.SetterDomain;

abstract class AbstractBeanConventionProxyFactory implements AOProxyFactory {

	protected final Map<Class<? extends Annotation>, Integer> indexes =new HashMap<Class<? extends Annotation>, Integer>(); 
	protected final Constructor<? extends Interceptor>[]  interceptors;
	
	
	@SuppressWarnings("unchecked")
	private final Class<? extends Interceptor>[]  classes = new Class[] {BasicGetterInterceptorImpl.class,BasicGetterCollectionInterceptorImpl.class,BasicGetterProxyInterceptorImpl.class,DomainGetterInterceptorImpl.class,SetterInterceptorImpl.class,DomainSetterInterceptorImpl.class, ExceptionTranslationListInterceptorImpl.class };
	
	@SuppressWarnings("unchecked")
	protected AbstractBeanConventionProxyFactory() {
		indexes();
		
		interceptors=new Constructor[indexes.size()]; 
		
		interseptors();
		
	}
	

	void interseptors()  {
	   
	    int i=0;
	    for(final Class<? extends Interceptor> clazz : classes) {
	    	interceptors[i]=newInterceptor(clazz);
	    	i++;
	    }
		
	  
	}

	private Constructor<? extends Interceptor> newInterceptor(final Class<? extends Interceptor> clazz)  {
		 try {
		return clazz.getDeclaredConstructor(ModelRepository.class);
		  } catch (final Exception ex ){
		    	throw new IllegalStateException(ex);
		    }
	} 

	protected void indexes() {
		indexes.put(Getter.class, 0);
		indexes.put(GetterProxyCollection.class, 1);
		indexes.put(GetterProxy.class, 2);
		indexes.put(GetterDomain.class, 3);
		indexes.put(Setter.class, 4);
		indexes.put(SetterDomain.class, 5);
		indexes.put(ExceptionTranslations.class, 6);
	} 

	
	
	
	protected final Integer index(final Method method, final Integer nvl) {
		for(final Annotation annotation : method.getDeclaredAnnotations()){
			if ( indexes.containsKey(annotation.annotationType()) ) {
				return indexes.get(annotation.annotationType());
			}
		}
		
		return nvl;
	}
	
	
	protected final Interceptor interceptor(final int index, final ModelRepository modelRepository) {
		try {
			return interceptors[index].newInstance(modelRepository);
		} catch (final Exception ex) {
			throw new IllegalArgumentException(ex);
		} 
	}
	
	protected final int numberOfInterceptors(){
		return interceptors.length;
	}
	
	

}
