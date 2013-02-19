package de.mq.mapping.util.proxy.support;

import java.lang.reflect.Method;

import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.mapping.util.proxy.Setter;

class SetterInterceptorImpl implements Interceptor {

	private ModelRepository modelRepository;
	
	public SetterInterceptorImpl(final ModelRepository modelRepository){
		this.modelRepository=modelRepository;
	}
	
	
	
	
	@Override
	public Object invoke(final Method method, final Object[] args) throws Throwable {
		setterArgumentGuard(method, args);
		setterAnnotationGuard(method);
	
		
	   modelRepository.put(method.getAnnotation(Setter.class).clazz(), method.getAnnotation(Setter.class).value(), args[0], method.getAnnotation(Setter.class).converter());
	   
		
		return null;
		
	}

	private void setterAnnotationGuard(final Method method) {
		if( ! method.isAnnotationPresent(Setter.class) ) {
			throw new IllegalStateException("Method " + method.getName() + " should be annotated with " + Setter.class.getSimpleName());
		}
	}

	private void setterArgumentGuard(final Method method, final Object[] args) {
		if (args.length != 1) {
			throw new IllegalArgumentException("Number of Argument for a setter should be 1: " + method);
		}
	}

	

	

}
