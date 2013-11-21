package de.mq.mapping.util.proxy.support;

import java.lang.reflect.Method;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.ModelRepository;

class BasicGetterInterceptorImpl implements Interceptor  {

	private final ModelRepository modelRepository;
	
	public BasicGetterInterceptorImpl(final ModelRepository modelRepository) {
		this.modelRepository=modelRepository;
	}

	

	@Override
	public final Object invoke(final Method method, final Object[] args) throws Throwable {
		annotationAwareGuard(method);
		if(method.getAnnotation(Getter.class).value().trim().length()==0){
			return  modelRepository.get(method.getAnnotation(Getter.class).clazz(), null, method.getAnnotation(Getter.class).converter(), method.getReturnType());
		}
		return  modelRepository.get(method.getAnnotation(Getter.class).clazz(), method.getAnnotation(Getter.class).value(), method.getAnnotation(Getter.class).converter(), method.getReturnType());
	}


	private void annotationAwareGuard(final Method method) {
		if( ! method.isAnnotationPresent(Getter.class) ) {
			throw new IllegalStateException("Method " + method.getName() + " should be annotated with " + Getter.class.getSimpleName());
		}
	}

	

}
