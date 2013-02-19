package de.mq.mapping.util.proxy.support;

import java.lang.reflect.Method;

import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.ModelRepository;


class DomainGetterInterceptorImpl implements Interceptor{

	
	private final ModelRepository modelRepository;
	
	public DomainGetterInterceptorImpl(final ModelRepository modelRepository){
		this.modelRepository=modelRepository;
	}
	
	@Override
	public final Object invoke(final Method method, final Object[] args) throws Throwable {
		methodAnnotationGuard(method);
		return modelRepository.get((Class<?>) method.getAnnotation(GetterDomain.class).clazz());
		
	}

	private void methodAnnotationGuard(final Method method) {
		if ( ! method.isAnnotationPresent(GetterDomain.class)) {
			throw new IllegalStateException("Method " + method.getName() + " should be annotated with " + GetterDomain.class.getSimpleName());
		}
	}

	

	

}
